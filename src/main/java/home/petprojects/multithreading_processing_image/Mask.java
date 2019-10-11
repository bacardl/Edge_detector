package home.petprojects.multithreading_processing_image;

public enum Mask {

    SOBEL(new double[][]{
            {-1, 0, +1},
            {-2, 0, +2},
            {-1, 0, +1},
    }, new double[][]{
            {+1, +2, +1},
            {0, 0, 0},
            {-1, -2, -1}}
    ),
    PREVITT(new double[][]{
            {-1, -1, -1},
            {0, 0, 0},
            {+1, +1, +1},
    }, new double[][]{
            {-1, 0, +1},
            {-1, 0, +1},
            {-1, 0, +1}}
    );

    private final double[][] x;
    private final double[][] y;

    Mask(double[][] x, double[][] y) {
        this.x = x;
        this.y = y;
    }

    public double[][] getX() {
        return x;
    }

    public double[][] getY() {
        return y;
    }
}
