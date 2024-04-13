import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardController extends KeyAdapter {
    private final Camera camera;
    private final JFrame frame;

    KeyboardController(Camera camera, JFrame frame) {
        this.camera = camera;
        this.frame = frame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                camera.setMovingForward(true);
                break;
            case KeyEvent.VK_S:
                camera.setMovingBackward(true);
                break;
            case KeyEvent.VK_A:
                camera.setMovingLeft(true);
                break;
            case KeyEvent.VK_D:
                camera.setMovingRight(true);
                break;
            case KeyEvent.VK_SPACE:
                camera.setMovingUp(true);
                break;
            case KeyEvent.VK_SHIFT:
                camera.setMovingDown(true);
                break;
            case KeyEvent.VK_ESCAPE:
                SwingUtilities.invokeLater(() -> {
                    frame.dispose();
                    System.exit(0);
                });
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                camera.setMovingForward(false);
                break;
            case KeyEvent.VK_S:
                camera.setMovingBackward(false);
                break;
            case KeyEvent.VK_A:
                camera.setMovingLeft(false);
                break;
            case KeyEvent.VK_D:
                camera.setMovingRight(false);
                break;
            case KeyEvent.VK_SPACE:
                camera.setMovingUp(false);
                break;
            case KeyEvent.VK_SHIFT:
                camera.setMovingDown(false);
                break;
        }
    }
}
