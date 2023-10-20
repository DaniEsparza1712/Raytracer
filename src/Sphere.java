import java.awt.*;

public class Sphere extends  Object3D{
    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Sphere(Vector3D position, double radius, Color color) {
        super(color, position);
        setRadius(radius);
        setMaterial(new Material(color, 0, 0, 0, 0, 0));
    }

    public Sphere(Vector3D position, double radius, Material newMaterial) {
        super(newMaterial.getColor(), position);
        setRadius(radius);
        setMaterial(newMaterial);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D L = Vector3D.subtract(ray.getOrigin(), getPosition());
        double tca = Vector3D.dotProduct(ray.getDirection(), L);
        double L2 = Math.pow(Vector3D.magnitude(L), 2);
        //Intersection
        double d2 = Math.pow(tca, 2) - L2 + Math.pow(getRadius(), 2);
        if(d2 >= 0){
            double d = Math.sqrt(d2);
            double t0 = -tca + d;
            double t1 = -tca - d;

            double distance = Math.min(t0, t1);
            Vector3D position = Vector3D.add(ray.getOrigin(), Vector3D.multiplyScalar(distance, ray.getDirection()));
            Vector3D normal = Vector3D.normalize(Vector3D.subtract(position, getPosition()));
            return new Intersection(position, distance, normal, this);
        }

        return null;
    }
}
