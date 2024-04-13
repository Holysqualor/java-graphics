public class Geometry {
    public static final float[] yAxis = new float[] {0, 1, 0};

    public static void multiplyMatrixByMatrix(final float[] A, final float[] B, float[] C) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                C[i * 4 + j] = 0.0f;
                for(int k = 0; k < 4; k++) {
                    C[i * 4 + j] += A[i * 4 + k] * B[k * 4 + j];
                }
            }
        }
    }

    public static float[] multiplyMatrixByVector(final float[] M, final float[] V) {
        float[] R = new float[4];
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                R[i] += M[i * 4 + j] * V[j];
            }
        }
        return R;
    }

    public static void normalizeVector(float[] V) {
        float l = (float) Math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2]);
        V[0] /= l;
        V[1] /= l;
        V[2] /= l;
    }

    public static float[] negateVector(float[] vector) {
        return new float[] {-vector[0], -vector[1], -vector[2]};
    }

    public static void addVectors(float[] A, final float[] B) {
        A[0] += B[0];
        A[1] += B[1];
        A[2] += B[2];
    }

    public static float[] crossProduct(final float[] A, final float[] B) {
        return new float[] {
                A[1] * B[2] - A[2] * B[1],
                A[2] * B[0] - A[0] * B[2],
                A[0] * B[1] - A[1] * B[0]
        };
    }

    public static void rotateVector(float[] A, final float[] B, final double theta) {
        float cosTheta = (float) Math.cos(theta);
        float sinTheta = (float) Math.sin(theta);

        float dot = dotProduct(A, B);
        float[] C = crossProduct(A, B);

        A[0] = cosTheta * A[0] + (1.0f - cosTheta) * dot * B[0] + sinTheta * C[0];
        A[1] = cosTheta * A[1] + (1.0f - cosTheta) * dot * B[1] + sinTheta * C[1];
        A[2] = cosTheta * A[2] + (1.0f - cosTheta) * dot * B[2] + sinTheta * C[2];
    }

    public static void multiplyVectorByScalar(float[] vector, final float scalar) {
        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }

    public static float dotProduct(final float[] A, final float[] B) {
        return A[0] * B[0] + A[1] * B[1] + A[2] * B[2];
    }

    public static float getDistance(final float[] A, final float[] B) {
        float x = B[0] - A[0];
        float y = B[1] - A[1];
        float z = B[2] - A[2];
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}
