package home.petprojects.multithreading_processing_image;

import processing.core.PApplet;
import processing.core.PImage;

public class EdgeDetectorAlgorithm {
    private final double[][] MASK_X;
    private final double[][] MASK_Y;

    public EdgeDetectorAlgorithm(Mask mask) {
        this.MASK_X = mask.getX();
        this.MASK_Y = mask.getY();
    }

    public void apply(PImage image, PImage newImage, int lowThreshold, int highThreshold, boolean gray) {
        int kernelSize = MASK_X.length;
        int kernelXY = kernelSize / 2;

        for (int x = 0; x < image.width; x++) {
            for (int y = lowThreshold; y < highThreshold; y++) {
                int rX = 0, gX = 0, bX = 0;
                int rY = 0, gY = 0, bY = 0;

                for (int k = -kernelXY; k <= kernelXY; k++) {
                    for (int l = -kernelXY; l <= kernelXY; l++) {
                        if (k * k + l * l < MASK_X.length * MASK_X.length) {
                            int p = image.get(x + k, y + l);

                            rX += ((p >> 16) & 0xFF) * MASK_X[k + kernelXY][l + kernelXY];
                            gX += ((p >> 8) & 0xFF) * MASK_X[k + kernelXY][l + kernelXY];
                            bX += (p & 0xFF) * MASK_X[k + kernelXY][l + kernelXY];

                            rY += ((p >> 16) & 0xFF) * MASK_Y[k + kernelXY][l + kernelXY];
                            gY += ((p >> 8) & 0xFF) * MASK_Y[k + kernelXY][l + kernelXY];
                            bY += (p & 0xFF) * MASK_Y[k + kernelXY][l + kernelXY];
                        }
                    }
                }

                int r = (int) Math.sqrt(rX * rX + rY * rY);
                int g = (int) Math.sqrt(gX * gX + gY * gY);
                int b = (int) Math.sqrt(bX * bX + bY * bY);

                r = PApplet.constrain(r, 0, 255);
                g = PApplet.constrain(g, 0, 255);
                b = PApplet.constrain(b, 0, 255);

                if (gray) {
                    int grayTone = (int) ((r * 0.2125) + (g * 0.7154) + (b * 0.0721));
                    r = grayTone;
                    g = grayTone;
                    b = grayTone;
                }

                int rgb = (r << 16) | (g << 8) | b;
                newImage.set(x, y, rgb);
            }
        }
        System.err.println(Thread.currentThread().getName() + ":" + "Done!");
    }
}
