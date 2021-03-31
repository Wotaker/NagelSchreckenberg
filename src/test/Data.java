package test;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Data {
    private ReportEngine engine;
    private Statistics test;
//    private double avgSpeed;
//    private double flow;
//    private double density;
    private int boardLength;
    private int boardHeight;
    private int iterations;
    private int maxSpeed;
    private final static File FILE_SINGLE = new File(
            "C:\\Users\\wojci\\OneDrive\\Pulpit\\AGH\\Semestr IV\\MS Dyskretnych\\Lab04\\nagel\\Single.csv");
    private final static File FILE_MULTI = new File(
            "C:\\Users\\wojci\\OneDrive\\Pulpit\\AGH\\Semestr IV\\MS Dyskretnych\\Lab04\\nagel\\Multi.csv");
    private final static int COUNT_SHIFT = 400;
    private final static int SAMPLE_SPAN = 100;

    public Data(
            int boardLength,
            int boardHeight,
            int maxSpeed,
            int iterations)
    {
        this.engine = new ReportEngine();
        this.boardLength = boardLength;
        this.boardHeight = boardHeight;
        this.maxSpeed = maxSpeed;
        this.iterations = iterations;
    }

    public void runTests(boolean multiLane, float[] probabilities) throws IOException{

        FileWriter outputFile;
        if (multiLane) outputFile = new FileWriter(FILE_MULTI);
        else outputFile = new FileWriter(FILE_SINGLE);

        CSVWriter writer = new CSVWriter(outputFile);
        String[] header = {"prob.", "density", "avgSpeed", "Flow"};
        writer.writeNext(header);

        for (double prob : probabilities){
            System.out.printf("\n===== Probability %f, MultiLane %b =====\n", prob, multiLane);
            int counter = 1;
            double step = 1.0 / SAMPLE_SPAN;
            for (double density = step; density <= 1; density += step){
                this.test = this.engine.runTest(
                        multiLane,
                        density,
                        iterations,
                        prob,
                        maxSpeed,
                        boardLength,
                        boardHeight,
                        COUNT_SHIFT
                );

                writer.writeNext(test.toCSV());
                if (counter % 25 == 0) {
                    System.out.printf(
                            "\tTest for density %f result:\n%s\n", density, test);
                    System.out.printf("Test%d saved...\n", counter);
                }
                counter++;
            }
        }

        writer.close();
    }
}
