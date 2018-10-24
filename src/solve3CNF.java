import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class solve3CNF {

	public static void main(String[] args) {
		Scanner inputStream = null;
		String fileName = args[0];
		int graphNumber = 0;
		solveClique sc = null;
		try {
			inputStream = new Scanner(new File(fileName));
		} catch (Exception e) {

		}
		System.out.printf("* Solve 3CNF in %s\n" + "   (reduced to K-Clique) *", args[0]);
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
			// Build clause groups - should not connect nodes that are in the same group
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
					//Check if current row is trying to make a connection to another node in its clause
					for (int k : clauseGroup) {
						if (j == k) {
							existInGroup = true;
							m[i][j] = 0;
							break;
						}
					}
					//Check to see if this value is the compliment
					if ((m[i][0]) == Math.negateExact(m[0][j]) && !existInGroup) {
						m[i][j] = 0;
						continue;
					}
					//Draw a connection
					if (!existInGroup) {
						m[i][j] = 1;
						degree[i]++;
						continue;
					}
					existInGroup = false;
				}
			}

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
			sc = new solveClique(newM.length, newM, degree);
			sc.search();

			// K clique number we are looking for
			int k = (newM.length)/3;
			//If solution is big enough to satisfy 3CNF - print solution
			if (sc.maxSize >= k) {
				boolean[] solutionAssignment = new boolean[numVar+1];
				System.out.print("3CNF No." + graphNumber + ":[n=" + numVar + " k=" + k + "] Assignments: [ ");
				//Need to copy solution to an array to display assignments
				for (int i = 0; i < sc.solution.length;i++) {
					if (sc.solution[i] == 1) {
						if (m[i+1][0] > 0) {
							solutionAssignment[m[i+1][0]] = true;
						}
						else {
							solutionAssignment[Math.abs(m[i+1][0])] = false;
						}
					}
					
				}
				//Print assignments
				for (int v = 1; v <solutionAssignment.length;v++) {
					char output =  solutionAssignment[v] ? 'T' : 'F';
					System.out.print(" " + "A" + v + "=" + output);
				}
				System.out.print(" ]" + " (" +(System.currentTimeMillis() - sc.cpuTime)
						+ " ms)");
				System.out.println();

			//Print no solution
			} else {
				System.out.print("3CNF No." + graphNumber + ":[n=" + numVar + " k=" + (k) + "] No "
						+ k + "-clique; no solution (" + (System.currentTimeMillis() - sc.cpuTime)
						+ " ms)");
				System.out.println();
			}

		}

	}
}
