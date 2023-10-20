import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Model3D extends Object3D{
    private List<Triangle> triangles;

    public Model3D(Vector3D position, Triangle[] triangles, Color color) {
        super(color, position);
        setTriangles(triangles);
        setMaterial(new Material(null, 0, 0, 0, 0, 0));
    }

    public Model3D(Vector3D position, Triangle[] triangles, Material newMaterial) {
        super(newMaterial,position);
        setTriangles(triangles);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(Triangle[] triangles) {
        Vector3D position = getPosition();
        Set<Vector3D> uniqueVertices = new HashSet<>();
        for(Triangle triangle : triangles){
            uniqueVertices.addAll(Arrays.asList(triangle.getPoints()));
        }

        for(Vector3D vertex : uniqueVertices){
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }

        this.triangles = Arrays.asList(triangles);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector3D normal = Vector3D.ZERO();
        Vector3D position = Vector3D.ZERO();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if (intersection != null && intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)) {
                distance = intersectionDistance;
                position = Vector3D.add(ray.getOrigin(), Vector3D.multiplyScalar(distance, ray.getDirection()));
                //normal = triangle.getNormal();
                normal = Vector3D.ZERO();
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                Vector3D[] normals = triangle.getNormals();
                for (int i = 0; i < uVw.length; i++) {
                    normal = Vector3D.add(normal, Vector3D.multiplyScalar(uVw[i], normals[i]));
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(position, distance, normal, this);
    }
}