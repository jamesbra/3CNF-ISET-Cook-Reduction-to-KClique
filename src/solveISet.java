import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class solveISet
{
	public static void main(String[] args)
	{

		Scanner inputStream = null;
		String fileName = args[0];
		ArrayList<Graph> graphList = new ArrayList<Graph>();
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
		File inFile = null;
		BufferedReader br = null;
		String currentRow; // row string read from file
		int size = 0; // number of vertices in graph
		int prevSize = 0; // Store previous size
		int count = 0; // count for current row number of matrix
		int edgeCount = 0;
		int graphNumber = 0; // store current graph number from file
		int[][] m = null; // 0/1 adjacency matrix
		int[] values = null; // temporary row values
		int[] degree = null; // number of edges that touch a vertex
		solveClique sc = null;
		try {
			inFile = new File(args[0]);
			// Create new FileReader to read input File
			br = new BufferedReader(new FileReader(inFile));

			System.out.printf("* Max Cliques in graphs in %s\n" + "   (|V|,|E|) Cliques (size, ms used) *", args[0]);
			while ((currentRow = br.readLine()) != null) {
				prevSize = size; // hold size
				if (size == count) {
					size = Integer.parseInt(currentRow);
					// System.out.println(currentRow);
					// System.out.println();
					if (m != null) {
						System.out.println();
						graphNumber++;
//						for (int[] i : m) {
//							for (int j : i) {
//								System.out.print(j + " ");
//							}
//							System.out.println();
//						}
						//solveClique(int size, int[][] m, int[] degree);
						sc = new solveClique(prevSize, m, degree);
						sc.search();
						sc.display(m);

						System.out.printf("\nG%d ( %d, %d ) {", graphNumber, prevSize, edgeCount / 2);
						int vCount = 0;
						for (int i = 0; i < prevSize; i++) {
							if (sc.solution[i] == 1) {
								vCount++;
								System.out.print(" " + i);
								if (i != prevSize - 1 && vCount != sc.maxSize) {
									System.out.print(",");
								}

							}
							if (i == prevSize - 1 || vCount == sc.maxSize) {
								System.out.print(" } ");
								break;
							}
						}
						System.out.printf("( size=%d, %d ms)", sc.maxSize, System.currentTimeMillis() - sc.cpuTime);
					}
					m = new int[size][size];
					degree = new int[size];
					Arrays.fill(degree, 0);
					edgeCount = 0;
					count = -1;
				} else {
					values = Arrays.stream(currentRow.split(" ")).mapToInt(Integer::parseInt).toArray();
					for (int i = 0; i < size; i++) {
						if (i == count) {
							m[count][i] = 0;
						} else {
							switch (values[i]) {
							case 1:
								//System.out.println("Case 1: "  + values[i]);
								m[count][i] = 0;
								break;
							case 0:
								//System.out.println("Case 0: " + values[i]);
								m[count][i] = 1;
								break;
							}
							if (m[count][i] == 1) {
								degree[i]++;
								edgeCount++;
							}
						}
					}
				}
				count++;
			}
		} catch (IOException e) {
			// Print Exception Stack Trace
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				// Print Exception Stack Trace
				ex.printStackTrace();
			}
			System.out.println("\n***");
		}

	}
}
