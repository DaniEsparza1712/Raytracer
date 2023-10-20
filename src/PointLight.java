import java.awt.*;

public class PointLight extends Light{

    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);
    }

    @Override
    public double getNDotL(Intersection intersection) {
        double base = 0.1;
        double li = getIntensity()/ (base + Math.pow(Vector3D.magnitude(Vector3D.subtract(getPosition(), intersection.getPosition())), 2));
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.multiplyScalar(li, Vector3D.subtract(getPosition(), intersection.getPosition()))), 0.0);
    }
}
