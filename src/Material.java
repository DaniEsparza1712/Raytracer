import java.awt.*;

public class Material {
    Color color;
    double shininess;
    float diffuseModifier;
    float reflectiveness;
    float refractiveness;
    float refractMixCoefficient;

    public Material(Color newColor, double shine, float diffuse, float reflectiveness, float refract, float refractMix){
        setColor(newColor);
        setShininess(shine);
        setDiffuseModifier(diffuse);
        setReflectiveness(reflectiveness);
        setRefractiveness(refract);
        setRefractMixCoefficient(refractMix);
    }

    public float getRefractMixCoefficient() {
        return refractMixCoefficient;
    }

    public void setRefractMixCoefficient(float refractMixCoefficient) {
        this.refractMixCoefficient = refractMixCoefficient;
    }

    public float getRefractiveness() {
        return refractiveness;
    }

    public void setRefractiveness(float refractiveness) {
        this.refractiveness = refractiveness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getShininess() {
        return shininess;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public float getDiffuseModifier() {
        return diffuseModifier;
    }

    public void setDiffuseModifier(float diffuseModifier) {
        this.diffuseModifier = diffuseModifier;
    }

    public float getReflectiveness() {
        return reflectiveness;
    }

    public void setReflectiveness(float reflectiveness) {
        this.reflectiveness = reflectiveness;
    }
}
