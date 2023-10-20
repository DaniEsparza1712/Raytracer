import java.awt.*;

public class Triangle implements IIntersectable{
    public static final double EPSILON = 0.0000001;
    private Vector3D[] points = new Vector3D[3];
    private Vector3D[] normals;
    private Color color;
    public Triangle(Vector3D v1, Vector3D v2, Vector3D v3){
        setPoints(v1, v2, v3);
        setNormals(null);
    }
    public Triangle(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D n1, Vector3D n2, Vector3D n3){
        setPoints(v1, v2, v3);
        setNormals(new Vector3D[]{n1, n2, n3});
    }
    public Triangle(Vector3D[] vertices){
        setPoints(vertices[0], vertices[1], vertices[2]);
        setNormals(null);
    }
    public Triangle(Vector3D[] vertices, Vector3D[] normals) {
        if(vertices.length == 3){
            setPoints(vertices[0], vertices[1], vertices[2]);
        }
        else{
            setPoints(Vector3D.ZERO, Vector3D.ZERO, Vector3D.ZERO);
        }
        setNormals(normals);
    }
    public Vector3D[] getPoints() {
        return points;
    }

    public void setPoints(Vector3D v1, Vector3D v2, Vector3D v3) {
        points[0] = v1;
        points[1] = v2;
        points[2] = v3;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Vector3D[] getNormals() {
        if (normals == null) {
            Vector3D normal = getNormal();
            setNormals(new Vector3D[]{normal, normal, normal});
        }
        return normals;
    }

    public Vector3D getNormal() {
        Vector3D normal = Vector3D.ZERO();
        Vector3D[] normals = this.normals;
        if (normals == null) {
            Vector3D[] vertices = getPoints();
            //1 to 0 and 2 to 0, multiplied by -1 or we can use 1 to 2 and 0 to 2
            Vector3D v = Vector3D.subtract(vertices[1], vertices[2]);
            Vector3D w = Vector3D.subtract(vertices[0], vertices[2]);
            normal = Vector3D.normalize(Vector3D.crossProduct(v, w));
        } else {
            for (int i = 0; i < normals.length; i++) {
                normal.setX(normal.getX() + normals[i].getX());
                normal.setY(normal.getY() + normals[i].getY());
                normal.setZ(normal.getZ() + normals[i].getZ());
            }
            normal.setX(normal.getX() / normals.length);
            normal.setY(normal.getY() / normals.length);
            normal.setZ(normal.getZ() / normals.length);
        }
        return normal;
    }

    public void setNormals(Vector3D[] normals) {
        this.normals = normals;
    }
    public void setNormals(Vector3D n1, Vector3D n2, Vector3D n3){
        this.normals = new Vector3D[]{n1, n2, n3};
    }

    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null);

        Vector3D[] vert = getPoints();
        Vector3D v2v0 = Vector3D.subtract(vert[2], vert[0]);
        Vector3D v1v0 = Vector3D.subtract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double determinant = Vector3D.dotProduct(v2v0, vectorP);
        double invDet = 1.0 / determinant;
        Vector3D vectorT = Vector3D.subtract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector3D.dotProduct(vectorT, vectorP);

        if (!(u < 0 || u > 1)) {
            Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
            double v = invDet * Vector3D.dotProduct(ray.getDirection(), vectorQ);
            if (!(v < 0 || (u + v) > (1.0 + EPSILON))) {
                double t = invDet * Vector3D.dotProduct(vectorQ, v1v0);
                intersection.setDistance(t);
            }
        }

        return intersection;
    }
}
