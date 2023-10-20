import java.awt.*;

public abstract class Object3D {
    private Color color;
    private  Vector3D position;
    private Material material;

    public Object3D(Color color, Vector3D position){
        this.color = color;
        this.position = position;
        this.material = new Material(null, 0, 0, 0, 0, 0);
    }
    public Object3D(Color color, Material newMaterial,Vector3D position){
        this.color = color;
        this.position = position;
        this.material = newMaterial;
    }
    public Object3D(Material newMaterial, Vector3D position){
        this.color = newMaterial.getColor();
        this.position = position;
        this.material = newMaterial;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public void setPosition(Vector3D pos){
        this.position = pos;
    }
    public Color getColor(){
        return this.color;
    }
    public Vector3D getPosition(){
        return this.position;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public abstract Intersection getIntersection(Ray ray);
}
