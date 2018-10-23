import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class solve3CNF {

	public static void main(String[] args) {
		Scanner inputStream = null;
		String fileName = args[0];
		int graphNumber = 0;
		int edgeCount = 0;
		solveClique sc = null;
		try {
			inputStream = new Scanner(new File(fileName));
		} catch (Exception e) {

		}
		System.out.printf("* Max Cliques in graphs in %s\n" + "   (|V|,|E|) Cliques (size, ms used) *", args[0]);
		System.out.println();
		while (inputStream.hasNextLine()) {
			HashMap<Integer, int[]> clauseIndexGroups = new HashMap<Integer, int[]>();

			int currentC = 1;

			// BUILD VARIABLE MAP - will fill out later
			String temp = inputStream.nextLine();
			String[] tokens = temp.split(" ");
			int numVar = Integer.parseInt(tokens[0]);
			if (numVar == 0) {
				break;
			}
			int size = tokens.length;


			int[][] m = new int[size][size];
			int[] degree = new int[size];

			for (int i = 1; i < size; i++) {

				int token = Integer.parseInt(tokens[i]);
				// Add to top column
				m[0][currentC] = token;
				currentC++;
				// Add to row
				m[i][0] = token;
			}
			//Build clause groups - should not connect nodes that are in the same group
			for (int i = 1; i < m.length - 1; i += 3) {
				int[] tempArray = { i, i + 1, i + 2 };
				clauseIndexGroups.put(i, tempArray);
				clauseIndexGroups.put(i + 1, tempArray);
				clauseIndexGroups.put(i + 2, tempArray);

			}

			// Building edge connections
			// ROW
			boolean existInGroup = false;
			for (int i = 1; i < m.length; i++) {
				// COLUMN
				for (int j = 1; j < m.length; j++) {
					int[] clauseGroup = clauseIndexGroups.get(i);
					for (int k : clauseGroup) {
						if (j == k) {
							existInGroup = true;
							m[i][j] = 0;
							break;
						}
					}
//					if (Math.abs(m[0][j]) == Math.abs(m[i][0]) && !existInGroup) {
						if ((m[i][0]) == Math.negateExact(m[0][j]) && !existInGroup) {
						m[i][j] = 0;
						continue;
					}
					if (!existInGroup) {
						m[i][j] = 1;
						degree[i]++;
						edgeCount++;
						continue;
					}
					existInGroup = false;
				}
			}
//
//			System.out.println();
//			for (int[] i: m) {
//				for (int j:i) {
//					System.out.print(j+"\t");
//				}
//				System.out.println();
//			}
//			
			

			int[][] newM = new int[m.length - 1][m.length - 1];
			int copyR = 0;
			int copyC = 0;

			// Shrink array to eliminate non 1/0s
			for (int i = 1; i < m.length; i++) {
				for (int j = 1; j < m.length; j++) {
					newM[copyR][copyC] = m[i][j];
					copyC++;
				}
				copyC = 0;
				copyR++;
			}

			graphNumber++;

			// SOLVE CLIQUE
			sc = new solveClique(numVar, newM, degree);
			sc.search();

			//System.out.print("3CNF No." + graphNumber+":[n="+numVar+" k="+(size/3)+"]");
			System.out.printf("\nG%d ( %d, %d ) {", graphNumber, size, edgeCount / 2);
			int vCount = 0;
			for (int i = 0; i < size; i++) {
				if (sc.solution[i] == 1) {
					vCount++;
					System.out.print(" " + i);
					if (i != size - 1 && vCount != sc.maxSize) {
						System.out.print(",");
					}

				}
				if (i == size - 1 || vCount == sc.maxSize) {
					System.out.print(" } ");
					break;
				}
			}
			System.out.printf("( size=%d, %d ms)", sc.maxSize, System.currentTimeMillis() - sc.cpuTime);
//			System.out.println();
//			for (int[] i: newM) {
//				for (int j:i) {
//					System.out.print(j+" ");
//				}
//				System.out.println();
//			}
//			sc.display(newM);
			
		}

	}
}
