package home.petprojects.multithreading_processing_image;

import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class EdgeDetector {
    //default img-source
    private String inputImagePath = Paths.get("").toAbsolutePath() +
            FileSystems.getDefault().getSeparator() + "images" +
            FileSystems.getDefault().getSeparator() + "test3.jpg";

    //default img-output
    private String outputImagePath = Paths.get("").toAbsolutePath() +
            FileSystems.getDefault().getSeparator() + "processed_images" +
            FileSystems.getDefault().getSeparator() + "processed.jpg";

    //default number of threads
    private int numberOfThreads = 1;

    //default mask-operator
    private Mask mask = Mask.SOBEL;

    public EdgeDetector() {
    }

    public EdgeDetector(String inputImagePath, String outputImagePath, int numberOfThreads, Mask mask) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
        this.numberOfThreads = numberOfThreads;
        this.mask = mask;
    }

    public String getInputImagePath() {
        return inputImagePath;
    }

    public void setInputImagePath(String inputImagePath) {
        this.inputImagePath = inputImagePath;
    }

    public String getOutputImagePath() {
        return outputImagePath;
    }

    public void setOutputImagePath(String outputImagePath) {
        this.outputImagePath = outputImagePath;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public Mask getMask() {
        return mask;
    }

    public void setMask(Mask mask) {
        this.mask = mask;
    }

    public void processedImage() {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(inputImagePath));
        } catch (IOException e) {
            System.err.println("Cannot read the image." + e);
        }

        PImage image = new PImage(myPicture);
        ImageTransformer imageTransformer = new ImageTransformer(image, numberOfThreads, new EdgeDetectorAlgorithm(mask));
        PImage transformedImage = imageTransformer.processImage();

        try {
            ImageIO.write((RenderedImage) transformedImage.getImage(), "jpg", new File(outputImagePath));
        } catch (IOException e) {
            System.err.println("Cannot write the image." + e);
        }
    }


}
