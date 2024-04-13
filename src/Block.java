import java.awt.*;

public class Block {
    private static final float DELTA = 1e-5f;
    public static final float[][] FACES = new float[][] {
            {-1, 0, 0},
            {1, 0, 0},
            {0, -1, 0},
            {0, 1, 0},
            {0, 0, -1},
            {0, 0, 1}
    };
    public static final int[][] VERTEX_INDEXES = {
            {7, 3, 1, 5},
            {0, 2, 6, 4},
            {0, 7, 3, 2},
            {5, 1, 6, 4},
            {2, 3, 1, 6},
            {0, 7, 5, 4}
    };

    private final float[][] vertices;
    private final float[][] facePosition = new float[6][3];
    private final Color color;

    public Block(float x, float y, float z, Color color) {
        this.color = color;
        vertices = new float[][] {
                {x, y, z, 1.0f},
                {x + 1.0f, y + 1.0f, z + 1.0f, 1.0f},
                {x, y + 1.0f, z, 1.0f},
                {x, y + 1.0f, z + 1.0f, 1.0f},
                {x + 1.0f, y, z, 1.0f},
                {x + 1.0f, y, z + 1.0f, 1.0f},
                {x + 1.0f, y + 1.0f, z, 1.0f},
                {x, y, z + 1.0f, 1.0f}
        };
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 4; j++) {
                Geometry.addVectors(facePosition[i], vertices[VERTEX_INDEXES[i][j]]);
            }
            Geometry.multiplyVectorByScalar(facePosition[i], 0.25f);
        }
    }

    public float[] getFacePosition(int index) {
        return facePosition[index];
    }

    public Color getColor() {
        return color;
    }

    public float[] getPosition() {
        return new float[] {vertices[0][0], vertices[0][1], vertices[0][2]};
    }

    public float[][] getVertices() {
        return vertices;
    }

    public float intersects(Ray ray) {
        float xMin = (vertices[ray.sign[0]][0] - ray.origin[0]) / ray.direction[0];
        float xMax = (vertices[1 - ray.sign[0]][0] - ray.origin[0]) / ray.direction[0];
        float yMin = (vertices[ray.sign[1]][1] - ray.origin[1]) / ray.direction[1];
        float yMax = (vertices[1 - ray.sign[1]][1] - ray.origin[1]) / ray.direction[1];
        if(xMin > yMax || yMin > xMax) {
            return -1.0f;
        }
        xMin = Math.max(yMin, xMin);
        xMax = Math.min(xMax, yMax);
        float zMin = (vertices[ray.sign[2]][2] - ray.origin[2]) / ray.direction[2];
        float zMax = (vertices[1 - ray.sign[2]][2] - ray.origin[2]) / ray.direction[2];
        if((xMin > zMax) || (zMin > xMax)) {
            return -1.0f;
        }
        float distance = Math.min(Math.max(xMin, zMin), Math.min(xMax, zMax));
        return (distance > 0 && distance < Constant.DRAW_DISTANCE) ? distance : -1.0f;
    }

    public int getFaceOfCollision(float[] collision) {
        if(Math.abs(collision[0] - vertices[0][0]) < DELTA) {
            return 0;
        }
        if(Math.abs(collision[0] - vertices[1][0]) < DELTA) {
            return 1;
        }
        if(Math.abs(collision[1] - vertices[0][1]) < DELTA) {
            return 2;
        }
        if(Math.abs(collision[1] - vertices[1][1]) < DELTA) {
            return 3;
        }
        if(Math.abs(collision[2] - vertices[0][2]) < DELTA) {
            return 4;
        }
        return 5;
    }
}
