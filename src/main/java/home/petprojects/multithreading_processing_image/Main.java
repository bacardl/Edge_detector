package home.petprojects.multithreading_processing_image;

import org.apache.commons.cli.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws ParseException {
        Options options = initOptions();

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(options, args);

        EdgeDetector edgeDetector = new EdgeDetector();

        if (commandLine.hasOption("opr")) {
            String[] arguments = commandLine.getOptionValues("opr");
            switch (arguments[0]) {
                case "sobel":
                    edgeDetector.setMask(Mask.SOBEL);
                    break;
                case "previtt":
                    edgeDetector.setMask(Mask.PREVITT);
                    break;
                default:
                    System.err.println("Required mask doesn't exist. Default mask('sobel') is used.");
            }
            System.out.println("Set up mask-operator: " + edgeDetector.getMask().name());
        }

        if (commandLine.hasOption("i")) {
            String[] arguments = commandLine.getOptionValues("i");
            String inputPath = arguments[0];
            if (isValidPath(inputPath)) {
                edgeDetector.setInputImagePath(inputPath);
            }
            System.out.println("Set up the input image path: " + edgeDetector.getInputImagePath());
        }

        if (commandLine.hasOption("o")) {
            String[] arguments = commandLine.getOptionValues("o");
            String outputPath = arguments[0];
            if (isValidPath(outputPath)) {
                edgeDetector.setOutputImagePath(outputPath);
            }
            System.out.println("Set up the output image path: " + edgeDetector.getOutputImagePath());
        }

        if (commandLine.hasOption("m")) {
            String[] arguments = commandLine.getOptionValues("m");
            int maxThreads = Integer.parseInt(arguments[0]);
            if (maxThreads > 1) {
                edgeDetector.setNumberOfThreads(maxThreads);
            }
            System.out.println("Set up maximum of threads: " + edgeDetector.getNumberOfThreads());
        }


        if (commandLine.hasOption("r")) {
            System.out.println("Building performance report using image transformer settings...");
            PerfomanceCalculator.calculatePerformance(edgeDetector.getInputImagePath(),
                    edgeDetector.getNumberOfThreads(), new EdgeDetectorAlgorithm(edgeDetector.getMask()));
        } else {
            edgeDetector.processedImage();
        }

    }

    private static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    private static Options initOptions() {
        Option operatorOption = new Option("opr", "opr", true, "Mask-operator which using in the algorithm.");
        operatorOption.setArgs(1);

        Option imageOption = new Option("i", "image", true, "Path to .jpg image.");
        imageOption.setArgs(1);

        Option imageOutputOption = new Option("o", "output", true, "Path to output .jpg image.");
        imageOutputOption.setArgs(1);

        Option maxThreadsOption = new Option("m", "maxThreads", true, "Maximum threads for usage.");
        maxThreadsOption.setArgs(1);

        Option reportOption = new Option("r", "report", false, "Build a performance report.");

        Options options = new Options();
        options.addOption(operatorOption);
        options.addOption(imageOption);
        options.addOption(imageOutputOption);
        options.addOption(maxThreadsOption);
        options.addOption(reportOption);

        return options;
    }


}
