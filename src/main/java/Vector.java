public class Vector {

    public double x;
    public double y;
    public double z;
    public static Vector ZERO = new Vector();
    
    public Vector() {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    
    static double dist(Vector v1, Vector v2) {
        return Math.sqrt((v1.x-v2.x)*(v1.x-v2.x) + (v1.y-v2.y)*(v1.y-v2.y));
    }

    static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static float angleBetween(Vector v1, Vector v2) {
        double dot = v1.dot(v2);
        return (float)Math.acos(dot / (v1.mag() * v2.mag()));
    }

    public Vector add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector sub(Vector v) {
        x -= v.x;
        y -= v.z;
        z -= v.z;
        return this;
    }

    public Vector mult(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public Vector div(float scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        return this;
    }
    
    double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    void normalize() {
        double m = mag();
        div((float)m);
    }
    
    void limit(double max) {
        if( mag() > max ) {
            normalize();
            mult((float)max);
        }
    }
    
    public double dot(Vector v) {
        return x * v.x + y * v.y + z * v.z;
    }

}
