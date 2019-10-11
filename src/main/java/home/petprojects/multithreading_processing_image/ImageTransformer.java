package home.petprojects.multithreading_processing_image;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageTransformer{
    private PImage image;
    private int numberOfThreads;
    private List<Integer> heightThresholds;
    private EdgeDetectorAlgorithm algorithm;

    ImageTransformer(PImage image, int numberOfThreads, EdgeDetectorAlgorithm algorithm) {
        this.image = image;
        this.numberOfThreads = numberOfThreads;
        this.heightThresholds = getThresholds(image, numberOfThreads);
        this.algorithm = algorithm;
    }

    public PImage processImage() {
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        PImage newImage = new PImage(image.width, image.height, image.format);

        for (int i = 0; i < heightThresholds.size() - 1; i++) {
            int lowThreshold = heightThresholds.get(i);
            int highThreshold = heightThresholds.get(i + 1);
            pool.execute(() -> {
                algorithm.apply(image, newImage, lowThreshold, highThreshold, true);
            });
        }

        System.err.println(Thread.currentThread().getName() + ":" + "Try shutdown poll!");

        pool.shutdown();
        while (!pool.isTerminated()) {
        }
//        try {
//            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
//                pool.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            pool.shutdownNow();
//        }
        System.err.println(Thread.currentThread().getName() + ":" + "Poll has been closed!");
        return newImage;
    }

    private List<Integer> getThresholds(PImage image, int numberOfThreads) {
        int pixelsHeightPerPart = image.height / numberOfThreads;

        List<Integer> heightThresholds = IntStream.rangeClosed(0, numberOfThreads)
                .boxed()
                .map(v-> v * pixelsHeightPerPart)
                .collect(Collectors.toList());

        if (pixelsHeightPerPart % numberOfThreads != 0) {
            heightThresholds.set(numberOfThreads, image.height);
        }
        return heightThresholds;
    }

}
