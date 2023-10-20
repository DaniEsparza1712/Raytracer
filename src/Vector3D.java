import java.util.Vector;

public class Vector3D {
    public double x, y, z;
    public static final Vector3D ZERO = new Vector3D(0,0,0);
    public Vector3D(double num1, double num2, double num3){
        x = num1;
        y = num2;
        z = num3;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public static double dotProduct(Vector3D vector1, Vector3D vector2){
        return (vector1.x * vector2.x) + (vector1.y * vector2.y) + (vector1.z * vector2.z);
    }
    public static Vector3D crossProduct(Vector3D vector1, Vector3D vector2){
        double new_x = (vector1.y * vector2.z) - (vector2.y * vector1.z);
        double new_y = (vector2.x * vector1.z) - (vector2.z * vector1.x);
        double new_z = (vector1.x * vector2.y) - (vector2.x * vector1.y);

        return new Vector3D(new_x, new_y, new_z);
    }
    public static Vector3D add(Vector3D vector1, Vector3D vector2){
        double new_x = vector1.x + vector2.x;
        double new_y = vector1.y + vector2.y;
        double new_z = vector1.z + vector2.z;

        return new Vector3D(new_x, new_y, new_z);
    }
    public static Vector3D subtract(Vector3D vector1, Vector3D vector2){
        double new_x = vector1.x - vector2.x;
        double new_y = vector1.y - vector2.y;
        double new_z = vector1.z - vector2.z;

        return new Vector3D(new_x, new_y, new_z);
    }
    public static Vector3D multiplyScalar(double scalar, Vector3D vector){
        double new_x = vector.x * scalar;
        double new_y = vector.y * scalar;
        double new_z = vector.z * scalar;

        return new Vector3D(new_x, new_y, new_z);
    }

    public static double magnitude(Vector3D vector){
        return Math.sqrt(dotProduct(vector, vector));
    }

    public static Vector3D normalize(Vector3D vector){
        double mag = magnitude(vector);
        return new Vector3D(vector.x / mag, vector.y / mag, vector.z / mag);
    }
    public static double distanceBetween(Vector3D v1, Vector3D v2){
        double xResult = Math.pow(v2.getX() - v1.getX(), 2);
        double yResult = Math.pow(v2.getY() - v1.getY(), 2);
        double zResult = Math.pow(v2.getY() - v1.getY(), 2);

        return Math.abs(Math.sqrt(xResult + yResult + zResult));
    }

    public Vector3D clone(){
        return new Vector3D(x, y, z);
    }
    public static Vector3D ZERO(){
        return ZERO.clone();
    }
    @Override
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
