
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Shark extends Vehicle {

    public Shark(float x, float y) {
        super(x, y);
        maxSpeed = 7;
        maxForce = 0.03f;
        velocity = new Vector(10, 10);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform old = g2.getTransform();
        g2.translate((int)location.x, (int)location.y);
        g2.setColor(Color.red);
        if(velocity.y > 0) {
            g2.rotate(Vector.angleBetween(new Vector(1, 0),velocity));
            int x[] = {-25,-25,25};
            int y [] = {7,-7,0};
            g2.fillPolygon(x, y, 3);
        } else {
            g2.rotate(Vector.angleBetween(new Vector(-1, 0),velocity));
            int x[] = {-25,25,25};
            int y [] = {0,7,-7};
            g2.fillPolygon(x, y, 3);
        }
        g2.setTransform(old);
        g2.setColor(Color.BLACK);
    }
}
