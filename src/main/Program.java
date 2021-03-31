package main;

import javax.swing.JFrame;
import test.ReportEngine;
import test.Statistics;
import test.Data;

import java.io.IOException;

public class Program extends JFrame {

	private static final long serialVersionUID = 1L;
	private GUI gof;

	public Program() {
		setTitle("Sound simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gof = new GUI(this);
		gof.initialize(this.getContentPane());
		this.setSize(1024, 768);
		this.setVisible(true);
	}

	public static void main(String[] args) {

		float[] probabilities = {0.2f, 0.4f, 0.6f};
		Data data = new Data(
				250,
				20,
				5,
				2500
		);

		long startTime = System.nanoTime();

		try {
			data.runTests(true, probabilities);
			data.runTests(false, probabilities);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		System.out.println("Execution time in milliseconds : " +
				timeElapsed / 1000000 + "ms\n");


//		long startTime = System.nanoTime();

//		ReportEngine engine = new ReportEngine();
//		Statistics[] test = engine.runBoothTests(
//				0.001, 1600, 0.0, 5, 200, 5, 600);
//		System.out.printf(
//				"\tMultilane test1 result:\n%s\n\tSinglelane test1 result:\n%s\n\n", test[0], test[1]);
//
//		test = engine.runBoothTests(
//				0.2, 1600, 0.3, 5, 250, 20, 400);
//		System.out.printf(
//				"\tMultilane test2 result:\n%s\n\tSinglelane test2 result:\n%s\n\n", test[0], test[1]);


//		long endTime = System.nanoTime();
//		long timeElapsed = endTime - startTime;
//		System.out.println("Execution time in milliseconds : " +
//				timeElapsed / 1000000 + "\n");


//		new Program();
	}
}
