
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Vehicle {
    protected Vector location;
    protected Vector velocity;
    protected Vector acceleration;

    protected float maxSpeed;
    protected float maxForce;
    
    public Vehicle(float x, float y) {
        location = new Vector(x, y);
        velocity = new Vector();
        acceleration = new Vector();
        maxSpeed = 2;
        maxForce = .1f;
    }
    
    public void update() {
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);
        acceleration = new Vector();
        if(location.x < 0) velocity.x = -velocity.x;
        else if(location.x > MyCanvas.WIDTH) velocity.x = -velocity.x;
        if(location.y < 0) velocity.y = -velocity.y;
        else if(location.y > MyCanvas.HEIGHT) velocity.y = -velocity.y;
    }
    
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform savePoint = g2.getTransform();
        g2.translate((int)location.x, (int)location.y);
        g.setColor(new Color(0, 0, 100));
        if(velocity.y > 0) {
            g2.rotate(Vector.angleBetween(new Vector(1, 0),velocity));
            int x[] = {-5,-5,5};
            int y [] = {2,-2,0};
            g2.fillPolygon(x, y, 3);
        } else {
            g2.rotate(Vector.angleBetween(new Vector(-1, 0),velocity));
            int x[] = {-5,5,5};
            int y [] = {0,2,-2};
            g2.fillPolygon(x, y, 3);
        }
        g2.setTransform(savePoint);
    }

    public void flock(List<? extends Vehicle> vehicles) {
        acceleration
                .add(align(vehicles))
                .add(cohesion(vehicles))
                .add(separate(vehicles));
    }

    public void applyForce(Vector force) {
        acceleration.add(force);
    }
    
    public Vector seek(Vector target) {
        Vector desired = Vector.sub(target, location);
        
        desired.normalize();
        
        desired.mult(maxSpeed);
        
        Vector steer = Vector.sub(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }
    
    public Vector flee(Vector target) {
        if(Vector.dist(location, target) > 100) return Vector.ZERO; 
        
        Vector desired = Vector.sub(location, target);
        double d = desired.mag();
        desired.normalize();        
        desired.mult(maxSpeed);
        
        Vector steer = Vector.sub(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }

    public Vector separate(List<? extends Vehicle> vehicles) {
        float desireSeparation = 25;
        
        Vector sum = new Vector();
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            double d = Vector.dist(location, vehicle.location);
            if(d > 0 && d < desireSeparation) {
                Vector diff = Vector.sub(location, vehicle.location);
                diff.normalize();
                diff.div((float)d);
                sum.add(diff);
                count++;
            }
        }
        if(count > 0) {
            sum.div(count);
        }
        
        if(sum.mag() > 0) {
            sum.normalize();
            sum.mult(maxSpeed);
            sum.sub(velocity);
            sum.limit(maxForce);
            sum.mult(2);
            return sum;
        }
        return Vector.ZERO;
    }

    public Vector cohesion(List<? extends Vehicle> vehicles) {
        float dist = 50;
        Vector sum = new Vector();
        int count = 0;
        
        for (Vehicle vehicle : vehicles) {
            double d = Vector.dist(location, vehicle.location);
            if(d > 0 && d < dist) {
                sum.add(vehicle.location);
                count++;
            }
        }
        
        if(count > 0) {
            sum.div(count);
            return seek(sum);
        }
        return Vector.ZERO;
    }

    public Vector align(List<? extends Vehicle> vehicles) {
        Vector sum = new Vector();
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            double d = Vector.dist(location, vehicle.location);
            if(d < 50) {
                sum.add(vehicle.velocity);
                count++;
            }
        }
        if(count > 0) {
            sum.div(count);
            sum.normalize();
            sum.mult(maxSpeed);
            Vector steer = Vector.sub(sum, velocity);
            steer.limit(maxForce);
            return steer;
        } else {
            return Vector.ZERO;
        }
    }
}
