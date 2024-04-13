import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Camera {
    private float[] position;
    private final float[] forward = new float[] {0, 0, -1};
    private final float[] right = new float[] {1, 0, 0};
    private final float[] up = new float[] {0, 1, 0};
    private final float[] viewMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];
    private final float[] translationMatrix = new float[16];
    private final List<Block> scene = new LinkedList<>();
    private final float delay = 10.0f / Constant.MAX_FPS;

    private boolean movingForward = false;
    private boolean movingBackward = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;

    public Camera(float x, float y, float z) {
        position = new float[] {x, y, z};
        updateViewMatrix();
        scene.add(new Block(0, 0, -2, Color.PINK));
        scene.add(new Block(-1, 1, -2, Color.BLUE));
        scene.add(new Block(-2, 0, -2, Color.YELLOW));
        scene.add(new Block(-1, -1, -2, Color.GREEN));
    }

    public List<Block> getScene() {
        return scene;
    }

    public void updateViewMatrix() {
        rotationMatrix[0] = right[0];
        rotationMatrix[1] = right[1];
        rotationMatrix[2] = right[2];
        rotationMatrix[4] = up[0];
        rotationMatrix[5] = up[1];
        rotationMatrix[6] = up[2];
        rotationMatrix[8] = -forward[0];
        rotationMatrix[9] = -forward[1];
        rotationMatrix[10] = -forward[2];
        rotationMatrix[15] = 1.0f;
        translationMatrix[0] = 1.0f;
        translationMatrix[3] = -position[0];
        translationMatrix[5] = 1.0f;
        translationMatrix[7] = -position[1];
        translationMatrix[10] = 1.0f;
        translationMatrix[11] = -position[2];
        translationMatrix[15] = 1.0f;
        Geometry.multiplyMatrixByMatrix(rotationMatrix, translationMatrix, viewMatrix);
    }

    public void rotateYaw(final double theta) {
        Geometry.rotateVector(forward, Geometry.yAxis, theta);
        Geometry.normalizeVector(forward);
        Geometry.rotateVector(right, Geometry.yAxis, theta);
        Geometry.normalizeVector(right);
        Geometry.rotateVector(up, Geometry.yAxis, theta);
        Geometry.normalizeVector(up);
        updateViewMatrix();
    }

    public void rotatePitch(final double theta) {
        Geometry.rotateVector(forward, right, theta);
        Geometry.normalizeVector(forward);
        Geometry.rotateVector(up, right, theta);
        Geometry.normalizeVector(up);
        updateViewMatrix();
    }

    public void putBlock() {
        Ray ray = new Ray(getPosition(), getForward());
        Block target = null;
        float minDistance = Float.MAX_VALUE;
        for(Block block : scene) {
            float distance = block.intersects(ray);
            if(distance != -1 && distance < minDistance) {
                minDistance = distance;
                target = block;
            }
        }
        if(target != null) {
            float[] collisionPoint = getForward();
            Geometry.multiplyVectorByScalar(collisionPoint, minDistance);
            Geometry.addVectors(collisionPoint, position);
            float[] blockPosition = target.getPosition();
            Geometry.addVectors(blockPosition, Block.FACES[target.getFaceOfCollision(collisionPoint)]);
            scene.add(new Block(blockPosition[0], blockPosition[1], blockPosition[2], Color.BLACK));
        }
    }

    public void breakBlock() {
        Ray ray = new Ray(getPosition(), getForward());
        Block target = null;
        float minDistance = Float.MAX_VALUE;
        for(Block block : scene) {
            float distance = block.intersects(ray);
            if(distance != -1 && distance < minDistance) {
                minDistance = distance;
                target = block;
            }
        }
        if(target != null) {
            scene.remove(target);
        }
    }

    public void moveForward() {
        float[] newPosition = forward.clone();
        newPosition[1] = 0.0f;
        Geometry.normalizeVector(newPosition);
        Geometry.multiplyVectorByScalar(newPosition, delay);
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void moveBackward() {
        float[] newPosition = Geometry.negateVector(forward);
        newPosition[1] = 0.0f;
        Geometry.normalizeVector(newPosition);
        Geometry.multiplyVectorByScalar(newPosition, delay);
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void moveRight() {
        float[] newPosition = right.clone();
        newPosition[1] = 0.0f;
        Geometry.normalizeVector(newPosition);
        Geometry.multiplyVectorByScalar(newPosition, delay);
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void moveLeft() {
        float[] newPosition = Geometry.negateVector(right);
        newPosition[1] = 0.0f;
        Geometry.normalizeVector(newPosition);
        Geometry.multiplyVectorByScalar(newPosition, delay);
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void moveUp() {
        float[] newPosition = new float[] {0.0f, delay, 0.0f};
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void moveDown() {
        float[] newPosition = new float[] {0.0f, -delay, 0.0f};
        Geometry.addVectors(newPosition, position);
        position = newPosition;
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

    public void setMovingBackward(boolean movingBackward) {
        this.movingBackward = movingBackward;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void updatePosition() {
        if(movingForward) {
            moveForward();
        }
        if(movingBackward) {
            moveBackward();
        }
        if(movingLeft) {
            moveLeft();
        }
        if(movingRight) {
            moveRight();
        }
        if(movingUp) {
            moveUp();
        }
        if(movingDown) {
            moveDown();
        }
        updateViewMatrix();
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public float[] getPosition() {
        return position.clone();
    }

    public float[] getForward() {
        return forward.clone();
    }
}
