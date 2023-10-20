import java.awt.*;

public class Shaders {
    public static Vector3D getSpecular(Light light, Material material, Vector3D surfaceNormal, Vector3D lightDir, Vector3D viewDir){
        Vector3D halfVector = Vector3D.normalize(Vector3D.add(lightDir, viewDir));
        float specularIntensity = (float) Math.pow(Math.max(Vector3D.dotProduct(surfaceNormal, halfVector), 0), material.getShininess());
        float specularRed = light.getColor().getRed() * specularIntensity;
        float specularGreen = light.getColor().getGreen() * specularIntensity;
        float specularBlue = light.getColor().getBlue() * specularIntensity;
        Vector3D specularColor = new Vector3D(specularRed, specularGreen, specularBlue);

        return specularColor;
    }

    public static Vector3D getDiffuse(Light light, Material material, Vector3D point, Vector3D surfaceNormal, float nDotL) {
        Vector3D direction = Vector3D.normalize(Vector3D.subtract(light.getPosition(), point));
        float lightIntensity = (float) Math.max(Vector3D.dotProduct(surfaceNormal, direction), 0.0f);
        float diffuseRed = material.getColor().getRed() * light.getColor().getRed() * lightIntensity * nDotL * material.getDiffuseModifier() / 255.0f;
        float diffuseGreen = material.getColor().getGreen() * light.getColor().getGreen() * lightIntensity * nDotL * material.getDiffuseModifier() / 255.0f;
        float diffuseBlue = material.getColor().getBlue() * light.getColor().getBlue() * lightIntensity * nDotL * material.getDiffuseModifier() / 255.0f;
        Vector3D diffuseColor = new Vector3D(diffuseRed, diffuseGreen, diffuseBlue);

        return diffuseColor;
    }

    public static Color getBPColor(Vector3D specularColor, Vector3D diffuseColor, Material material, float ambientIntensity){
        float ambientRed = material.getColor().getRed() * ambientIntensity / 255.0f;
        float ambientGreen = material.getColor().getGreen() * ambientIntensity / 255.0f;
        float ambientBlue = material.getColor().getBlue() * ambientIntensity / 255.0f;
        Vector3D ambientColor = new Vector3D(ambientRed,ambientGreen, ambientBlue);

        double red = ambientColor.getX() + specularColor.getX() + diffuseColor.getX();
        double green = ambientColor.getY() + specularColor.getY() + diffuseColor.getY();
        double blue = ambientColor.getZ() + specularColor.getZ() + diffuseColor.getZ();

        Color finalColor = new Color(clamp(red, 0, 255) / 255.0f, clamp(green, 0, 255) / 255.0f, clamp(blue, 0, 255) / 255.0f);
        return finalColor;
    }



    public static float clamp(double value, double min, double max){
        if(value < min){
            return (float) min;
        }
        if(value > max){
            return (float) max;
        }
        else{
            return (float) value;
        }
    }
}
