import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame {
    public Game() {
        super("Minecraft");
        Camera camera = new Camera(0, 0, 0);
        Screen screen = new Screen(camera);
        setSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        add(screen);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage("");
        Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "blankCursor");
        setCursor(cursor);

        Timer timer = new Timer( 1000 / Constant.MAX_FPS, e -> {
            screen.repaint();
            camera.updatePosition();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                timer.start();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClicked(e, camera);
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e, camera);
            }
        };
        addKeyListener(new KeyboardController(camera, this));
        addMouseListener(adapter);
        addMouseMotionListener(adapter);

        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);
    }

    private void handleMouseClicked(MouseEvent e, Camera camera) {
        int key = e.getButton();
        if(key == MouseEvent.BUTTON1) {
            camera.breakBlock();
        } else if(key == MouseEvent.BUTTON3) {
            camera.putBlock();
        }
    }


    private void handleMouseMoved(MouseEvent e, Camera camera) {
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        int deltaX = e.getX() - halfWidth;
        int deltaY = e.getY() - halfHeight;
        float aspect = (float) getWidth() / getHeight();
        if(deltaX != 0) {
            camera.rotateYaw(Math.toRadians(Constant.SENSITIVITY * deltaX / halfWidth * aspect));
        }
        if(deltaY != 0) {
            camera.rotatePitch(Math.toRadians(Constant.SENSITIVITY * deltaY / halfHeight) * aspect);
        }
        try {
            Robot robot = new Robot();
            robot.mouseMove(getX() + halfWidth, getY() + halfHeight);
        } catch (AWTException awtException) {
            System.out.println(awtException.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}