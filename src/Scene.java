import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<Object3D> objects;
    private List<Light> lights;
    public Scene(){
        setObjects(new ArrayList<>());
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<Object3D> getObjects() {
        return objects;
    }
    public void addObject(Object3D object){
        getObjects().add(object);
    }

    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public void addLight(Light newLight){
        if(lights == null){
            lights = new ArrayList<Light>();
        }
        lights.add(newLight);
    }
}
