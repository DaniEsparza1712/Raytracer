import java.awt.*;

public abstract class Light extends Object3D{
    private double intensity;
    public Light(Vector3D position, Color color, double intensity){
        super(color, position);
        setIntensity(intensity);
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
    public abstract double getNDotL(Intersection intersection);
    public Intersection getIntersection(Ray ray){
        return new Intersection(Vector3D.ZERO, -1, Vector3D.ZERO, null);
    }
}
