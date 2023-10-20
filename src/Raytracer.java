import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Raytracer {
    public static final double EPSILON = 0.0000001;
    public static int counter;
    public static void main(String[] args) {
        System.out.println(new Date());

        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 80, 45, 640, 360, 0.6, 50.0));
        scene02.addLight(new DirectionalLight(new Vector3D(4,7,3), Color.WHITE, 5f));
        scene02.addLight(new PointLight(new Vector3D(3, 7, 7), Color.RED, 4f));
        scene02.addLight(new PointLight(new Vector3D(1.5, 5, 0), Color.CYAN, 4f));
        scene02.addObject(new Sphere(new Vector3D(0.0, 1.0, 5.0), 0.5, new Material(Color.CYAN, 90f, 1, 0, 0, 0)));
        scene02.addObject(new Sphere(new Vector3D(0.5, 1.0, 4.5), 0.25, new Material(Color.BLUE, 90f, 1, 0, 0, 0)));
        scene02.addObject(new Sphere(new Vector3D(0.15, 0.5, 4.5), 0.3, new Material(Color.YELLOW, 90f, 1, 0, 0, 0)));
        scene02.addObject(new Sphere(new Vector3D(4.85, 1.0, 4.5), 0.3, new Material(Color.PINK, 90f, 1, 0, 0, 0)));
        //scene02.addObject(new Sphere(new Vector3D(0.4, -1.5, 0.35), 0.4 ,new Material(Color.GREEN, 90, 1, 0.3f, 1, 0.5f)));
        scene02.addObject(new Sphere(new Vector3D(0.4, -1.5, 0.35), 0.1 ,new Material(Color.GREEN, 90, 1, 1f, 0f, 0)));
        //scene02.addObject(ObjReader.getModel3D("SmallTeapot.obj", new Vector3D(2, -2.2, 1.3), Color.BLUE,100f, 0.8f, 1f, 0.0f, 0f));
        scene02.addObject(ObjReader.getModel3D("floor.obj", new Vector3D(0,-2.2,0), Color.BLUE, 5.5f, 0.5f, 0, 0, 0));
        scene02.addObject(ObjReader.getModel3D("floor.obj", new Vector3D(0,5.5,0), Color.RED, 5.5f, 0.5f, 0, 0, 0));
        //scene02.addObject(ObjReader.getModel3D("bg.obj", new Vector3D(-5.5f,0,7), Color.GREEN, 5.5f, 0.5f, 0, 0, 0));
        //scene02.addObject(ObjReader.getModel3D("bg.obj", new Vector3D(5.5f,0,7), Color.YELLOW, 5.5f, 0.5f, 0, 0, 0));
        //scene02.addObject(ObjReader.getModel3D("wall.obj", new Vector3D(0f,0,14), Color.BLACK, 5.5f, 0.5f, 0, 0, 0));
        //scene02.addObject(ObjReader.getModel3D("wall.obj", new Vector3D(0f,0,-5), Color.BLACK, 5.5f, 0.5f, 0, 0, 0));
        scene02.addObject(ObjReader.getModel3D("bottle.obj", new Vector3D(-0.5, 0, 5), Color.GREEN, 90f, 0.8f, 0.4f, 1, 0.8f));
        scene02.addObject(ObjReader.getModel3D("boat.obj", new Vector3D(-0.5, 0, 3), Color.ORANGE, 90f, 0.8f, 0.4f, 1, 0.8f));

        BufferedImage image = raytrace(scene02);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = new double[]{mainCamera.getNearPane(), mainCamera.getFarPane()};
        double cameraZ = mainCamera.getPosition().getZ();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();

        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null, new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});

                Color pixelColor = getColor(closestIntersection, ray, lights,mainCamera, objects, cameraZ, nearFarPlanes, 5);

                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
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
    public static Color mixColor(Color color01, Color color02, float mixCoefficient){
        mixCoefficient = clamp(mixCoefficient, 0, 1);
        float red = (color01.getRed()/255.0f * (1 - mixCoefficient) + color02.getRed()/255.0f * mixCoefficient);
        float blue = (color01.getBlue()/255.0f * (1 - mixCoefficient) + color02.getBlue()/255.0f * mixCoefficient);
        float green = (color01.getGreen()/255.0f * (1 - mixCoefficient) + color02.getGreen()/255.0f * mixCoefficient);

        return new Color(red, green, blue);
    }
    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int k = 0; k < objects.size(); k++) {
            Object3D currentObj = objects.get(k);
            if (caster == null || !currentObj.equals(caster)) {
                Intersection intersection = currentObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
    public static Vector3D getRefractDir(Material intersMaterial, Vector3D normal, Vector3D i){
        double n1 = 1;
        double n = n1/intersMaterial.refractiveness;
        double c1 = Vector3D.dotProduct(normal, i);
        double c2 = Math.sqrt(1 - Math.pow(n, 2) * (1 - Math.pow(c1, 2)));

        Vector3D t = Vector3D.add(Vector3D.multiplyScalar(n, i), Vector3D.multiplyScalar(n*c1 - c2, normal));
        return t;
    }
    public static Color getColor(Intersection closestIntersection, Ray ray, List<Light> lights, Camera mainCamera, List<Object3D> objects, double cameraZ, double[] nearFarPlanes, int bouncesLeft){
        if(bouncesLeft <= 0){
            return Color.BLACK;
        }
        Color matColor;
        Color reflectColor = Color.BLACK;
        Color refractColor = Color.BLACK;
        Color pixelColor = Color.BLACK;

        Vector3D diffuseColor = Vector3D.ZERO;
        Vector3D specularColor = Vector3D.ZERO;
        if (closestIntersection != null) {
            for (Light light : lights){
                Vector3D castPos = Vector3D.add(closestIntersection.getPosition(), Vector3D.multiplyScalar(EPSILON, closestIntersection.getNormal()));
                Material material = closestIntersection.getObject().getMaterial();

                Vector3D surfaceNormal = closestIntersection.getNormal();
                Vector3D lightDir = Vector3D.normalize(Vector3D.subtract(light.getPosition(), closestIntersection.getPosition()));
                Vector3D viewDir = Vector3D.normalize(Vector3D.subtract(mainCamera.getPosition(), closestIntersection.getPosition()));
                float nDotL = (float) light.getNDotL(closestIntersection);

                Ray lightRay = new Ray(castPos, Vector3D.normalize(Vector3D.subtract(light.getPosition(),closestIntersection.getPosition())));
                Intersection shadowCheckIntersection = raycast(lightRay, objects, null, null);

                if(shadowCheckIntersection != null && shadowCheckIntersection.getObject() != closestIntersection.getObject()){
                    double occluderDistance = Vector3D.distanceBetween(closestIntersection.getPosition(), shadowCheckIntersection.getPosition());
                    double lightDistance = Vector3D.distanceBetween(closestIntersection.getPosition(), light.getPosition());

                    if(occluderDistance > lightDistance){
                        specularColor = Vector3D.add(specularColor, Shaders.getSpecular(light, material, surfaceNormal, lightDir, viewDir));
                        diffuseColor = Vector3D.add(diffuseColor, Shaders.getDiffuse(light, material, closestIntersection.getPosition(), surfaceNormal, nDotL));
                    }
                }
                else{
                    specularColor = Vector3D.add(specularColor, Shaders.getSpecular(light, material, surfaceNormal, lightDir, viewDir));
                    diffuseColor = Vector3D.add(diffuseColor, Shaders.getDiffuse(light, material, closestIntersection.getPosition(), surfaceNormal, nDotL));
                }
            }
            matColor = Shaders.getBPColor(specularColor, diffuseColor, closestIntersection.getObject().getMaterial(), 0.01f);


            Vector3D castPos = Vector3D.add(closestIntersection.getPosition(), Vector3D.multiplyScalar(EPSILON, closestIntersection.getNormal()));
            Ray reflectiveRay = new Ray(castPos, closestIntersection.getNormal());
            Intersection reflectionIntersection = raycast(reflectiveRay, objects, null, null);

            if(reflectionIntersection != null){
                reflectColor = getColor(reflectionIntersection, reflectiveRay, lights,mainCamera, objects, cameraZ, nearFarPlanes, bouncesLeft-1);
            }
            pixelColor = mixColor(matColor, reflectColor, closestIntersection.getObject().getMaterial().getReflectiveness());

            Vector3D refractDir = getRefractDir(closestIntersection.getObject().getMaterial(), closestIntersection.getNormal(), ray.getDirection());
            Vector3D refractCastPos = Vector3D.add(closestIntersection.getPosition(), Vector3D.multiplyScalar(-1 * EPSILON, closestIntersection.getNormal()));

            Ray refractiveRay = new Ray(refractCastPos, refractDir);
            Intersection refractIntersection = raycast(refractiveRay, objects, closestIntersection.getObject(), null);

            if(refractIntersection != null){
                refractColor = getColor(refractIntersection, refractiveRay, lights, mainCamera, objects, cameraZ, nearFarPlanes, bouncesLeft-1);
            }
            pixelColor = mixColor(pixelColor, refractColor, closestIntersection.getObject().getMaterial().getRefractMixCoefficient());
        }
        return pixelColor;
    }
}
