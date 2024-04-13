public class Ray {
    public final float[] origin;
    public final float[] direction;
    public final int[] sign;

    public Ray(float[] origin, float[] direction) {
        this.origin = origin;
        this.direction = direction;
        sign = new int[] {direction[0] < 0 ? 1 : 0, direction[1] < 0 ? 1 : 0, direction[2] < 0 ? 1 : 0};
    }
}