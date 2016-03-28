
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class MyCanvas extends Canvas implements MouseMotionListener {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 600;

    private static final int TOTAL_FISH = 150;
    private static final int TOTAL_SHARK = 4;

    private Thread thread;
    private List<Vehicle> vehicles;
    private List<Shark> sharks = new ArrayList<>();
    private RenderingHints hints;
    private int mouseX = 0;
    private int mouseY = 0;

    public MyCanvas() {
        setSize(WIDTH, HEIGHT);
        thread = new Thread(r);
        hints  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        vehicles = new ArrayList<>();

        for(int i = 0; i < TOTAL_FISH; i++) {
            Vehicle newVehicle = new Vehicle(getWidth()/2, getHeight()/2);
            newVehicle.velocity = new Vector(Math.random()*10-5 , Math.random()*10-5);
            vehicles.add(newVehicle);
        }

        for (int i = 0; i < TOTAL_SHARK; i++) {
            sharks.add(new Shark((float)Math.random() * WIDTH, (float)Math.random() * HEIGHT));
        }

        addMouseMotionListener(this);
    }

    private void update() {
        vehicles.stream().forEach(vehicle -> {
            vehicle.seek(new Vector(mouseX, mouseY));
            vehicle.flock(vehicles);

            sharks.stream().forEach(shark -> {
                Vector fleeVector = vehicle.flee(shark.location);
                fleeVector.mult(5);
                vehicle.applyForce(fleeVector);
            });

            vehicle.update();
        });

        sharks.stream().forEach(shark -> {
            shark.applyForce(shark.separate(sharks));
            shark.applyForce(shark.seek(new Vector(mouseX, mouseY)));
            shark.update();
            shark.applyForce(shark.seek(new Vector(mouseX, mouseY)));
        });
    }

    void paint() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D) (bs.getDrawGraphics());
        g.setRenderingHints(hints);
        g.clearRect(0, 0, getWidth(), getHeight());

        
        for (Vehicle vehicle1 : vehicles) {
            vehicle1.render(g);
        }
        for (Shark shark : sharks) {
            shark.render(g);
        }

        g.dispose();
        bs.show();
    }


    Runnable r = () -> {
        long before = System.nanoTime();
        double delta = 0;
        double ns = 1000000000.0 / 60.0;

        long lastTime = System.currentTimeMillis();
        while(true) {
            long now = System.nanoTime();
            delta += (now - before) / ns;
            while( delta > 1 ) {
                delta--;
                update();
            }
            before = System.nanoTime();
            paint();
            if(System.currentTimeMillis() - lastTime > 1000) {
                lastTime = System.currentTimeMillis();
            }
        }
    };

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        MyCanvas canvas = new MyCanvas();
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        canvas.thread.start();
    }
}
