package com.jujutsu.tsne;

import java.io.File;

import javax.swing.JFrame;

import org.math.io.files.BinaryFile;
import org.math.io.parser.ArrayString;
import org.math.plot.FrameView;
import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;
import org.math.plot.plots.ScatterPlot;

public class TSneBinaryDemo {

	static double perplexity = 5.0;
	private static int initial_dims = 50;

	public TSneBinaryDemo() {}

	public static void runTSne(double [][] matrix) {
		System.out.println("Shape is: " + matrix.length + " x " + matrix[0].length);
		double [][] Y = TSne.tsne(matrix, 2, initial_dims, perplexity, 2500, false);
		System.out.println("Result is = " + Y.length + " x " + Y[0].length + " => \n" + ArrayString.printDoubleArray(Y));
		Plot2DPanel plot = new Plot2DPanel();

		ScatterPlot dataPlot = new ScatterPlot("Data", PlotPanel.COLORLIST[0], Y);
		plot.plotCanvas.setNotable(true);
		plot.plotCanvas.setNoteCoords(true);
		plot.plotCanvas.addPlot(dataPlot);

		FrameView plotframe = new FrameView(plot);
		plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		plotframe.setVisible(true);
	}

	static double[][] doubleArrayToMatrix(double[] d, int rows, int cols) {
		double [][] matrix = new double[rows][cols];
		int idx = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = d[idx++];
			}
		}
		return matrix;
	}

	static double[][] floatArrayToMatrix(float[] d, int rows, int cols) {
		double [][] matrix = new double[rows][cols];
		int idx = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = d[idx++];
			}
		}
		return matrix;
	}

	static double[][] intArrayToMatrix(int[] d, int rows, int cols) {
		double [][] matrix = new double[rows][cols];
		int idx = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = d[idx++];
			}
		}
		return matrix;
	}

	public static double [][] loadData(String [] args) {
		String usage = "Usage: TSneBinaryDemo [options] -asMatrix ROWS COLS file \nAvailable options:\n  -endian <big|little, default = big>\n  -data <double|float|int, default = double>" 
				     + "Example: TSneBinaryDemo -data double -asMatrix 1000 20 Theta_DxK_1000_20_05000.BINARY";
		
		File file = null;
		String dataType = "double";
		String endianness = BinaryFile.BIG_ENDIAN;
		int rows = 0;
		int cols = 0;

		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("-endian")) {
				if (args[i + 1].equals("little"))
					endianness = BinaryFile.LITTLE_ENDIAN;
				i++;
			} else if (args[i].equals("-data")) {
				dataType = args[i + 1];
				i++;
			} else if (args[i].equals("-asMatrix")) {
				String rowS = args[i + 1];
				rows = Integer.parseInt(rowS);
				i++;
				String colS = args[i + 1];
				cols = Integer.parseInt(colS);
				i++;
			} else {
				file = new File(args[i]);
				if (!file.exists()) {
					System.out.println("File " + file + " doesn't exists.\n" + usage);
					return null;
				}
				i++;
			}
		}

		if (dataType.equals("double")) {
			double[] d = BinaryFile.readDoubleArray(file, endianness);
			double [][] matrix = doubleArrayToMatrix(d,rows,cols);
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[i][j] + ",");
				}
				System.out.println();
			}              	
			return matrix;
		} else if (dataType.equals("float")) {
			float[] d = BinaryFile.readFloatArray(file, endianness);
			double [][] matrix = floatArrayToMatrix(d,rows,cols);
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[i][j] + ",");
				}
				System.out.println();
			}              	
			return matrix;				
		} else if (dataType.equals("int")) {
			int[] d = BinaryFile.readIntArray(file, endianness);
			double [][] matrix = intArrayToMatrix(d,rows,cols);
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[i][j] + ",");
				}
				System.out.println();
			}
			return matrix;	
		} else {
			System.out.println(usage);
		}
		return null;
	}

	public static void main(String[] args) {
		double [][] matrix = loadData(args);
		if(matrix!=null) runTSne(matrix);
	}
}
