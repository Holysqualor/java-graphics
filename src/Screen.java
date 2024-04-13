import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Screen extends JPanel {
    private final float[] projectionMatrix;

    private final Camera camera;

    public Screen(Camera camera) {
        this.camera = camera;
        float ratio = (float) Constant.SCREEN_WIDTH / Constant.SCREEN_HEIGHT;
        projectionMatrix = createProjectionMatrix(ratio, -ratio, 1.0f, -1.0f, Constant.DRAW_DISTANCE, 1);
    }

    public float[] createProjectionMatrix(final float r, final float l, final float t, final float b, final float f, final float n) {
        float x = r - l;
        float y = t - b;
        float z = f - n;
        float d = 2.0f * n;
        return new float[] {
                d / x, 0, (r + l) / x, 0,
                0, d / y, (t + b) / y, 0,
                0, 0, -(f + n) / z, (-d * f) / z,
                0, 0, -1.0f, 0
        };
    }

    public int[][] projectVertices(float[][] blockVertices) {
        int[][] points = new int[8][2];
        for(int i = 0; i < 8; i++) {
            float[] C = Geometry.multiplyMatrixByVector(projectionMatrix, Geometry.multiplyMatrixByVector(camera.getViewMatrix(), blockVertices[i]));

            if(C[3] <= 0.0f) {
                return null;
            }
            C[0] /= C[3];
            C[1] /= C[3];
            points[i] = new int[]{(int) ((C[0] + 1.0f) * 0.5f * Constant.SCREEN_WIDTH), (int) ((1.0f - C[1]) * 0.5f * Constant.SCREEN_HEIGHT)};
        }
        return points;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        float[] cameraPosition = camera.getPosition();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        List<Layer> canvas = new ArrayList<>();
        List<Block> scene = camera.getScene();
        Color blockColor;
        for(Block block : scene) {
            if(Geometry.getDistance(cameraPosition, block.getPosition()) < Constant.DRAW_DISTANCE) {
                int[][] points = projectVertices(block.getVertices());
                if(points == null) {
                    continue;
                }
                blockColor = block.getColor();
                for(int i = 0; i < 6; i++) {
                    for(int j = 0; j < 4; j++) {
                        xPoints[j] = points[Block.VERTEX_INDEXES[i][j]][0];
                        yPoints[j] = points[Block.VERTEX_INDEXES[i][j]][1];
                    }
                    canvas.add(new Layer(new Polygon(xPoints, yPoints, 4), Geometry.getDistance(cameraPosition, block.getFacePosition(i)), blockColor));
                }
            }
        }
        canvas.sort((layer1, layer2) -> Float.compare(layer2.distanceToCamera(), layer1.distanceToCamera()));
        for(Layer layer : canvas) {
            g.setColor(layer.color());
            g.fillPolygon(layer.blockFace());
        }
        g.setColor(Color.RED);
        g.fillRect(Constant.SCREEN_WIDTH / 2 - 9, Constant.SCREEN_HEIGHT / 2, 20, 2);
        g.fillRect(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGHT / 2 - 9, 2, 20);
    }
}
