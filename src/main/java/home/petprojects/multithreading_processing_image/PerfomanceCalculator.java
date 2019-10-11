package home.petprojects.multithreading_processing_image;

import processing.core.PImage;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Line;
import tech.tablesaw.plotly.traces.ScatterTrace;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class PerfomanceCalculator {
    public static void calculatePerformance(String inputImagePath, int maxOfThreads, EdgeDetectorAlgorithm algorithm) {
        int[] options = IntStream.rangeClosed(1, maxOfThreads).toArray();
        long[] period = new long[options.length];

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(inputImagePath));
        } catch (IOException e) {
            System.err.println("Cannot read the image." + e);
        }

        PImage image = new PImage(myPicture);

        for (int option : options) {
            final long before = System.nanoTime();
            new ImageTransformer(image, option, algorithm).processImage();
            final long after = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - before);
            period[option - 1] = after;
        }
        displayPlot(options, period);
    }

    private static void displayPlot(int[] numberOdThread, long[] period) {
        Table results =
                Table.create("Statistic")
                        .addColumns(
                                IntColumn.create("numberOfThread", numberOdThread),
                                LongColumn.create("period", period));


        NumericColumn<?> x = results.nCol("numberOfThread");
        NumericColumn<?> y = results.nCol("period");

        Axis xAxis = Axis.builder()
                .title("Number of threads")
                .build();
        Axis yAxis = Axis.builder()
                .title("Time(ms)")
                .build();

        Layout layout =
                Layout.builder()
                        .title("Dependence of time on the number of threads")
                        .xAxis(xAxis)
                        .yAxis(yAxis)
                        .showLegend(true)
                        .build();
        ScatterTrace trace =
                ScatterTrace.builder(x, y)
                        .mode(ScatterTrace.Mode.LINE)
                        .line(Line.builder().shape(Line.Shape.SPLINE).smoothing(1.2).build())
                        .build();
        Plot.show(new Figure(layout, trace));
    }
}
