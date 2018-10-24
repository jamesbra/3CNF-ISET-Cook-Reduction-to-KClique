import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
Adjacency Matrix:
	Each row corresponds to a vertex and so does each column
	Always symmetric and square
	
Instance Matrix:
	Each row represents a vertex and each column represents an edge
	Doesn't always Have to be square

*/
public class solveClique {
	// The basic clique solver

	long nodes; // number of decisions
	long timeLimit; // milliseconds
	long cpuTime; // milliseconds
	int maxSize; // size of max clique
	int style; // for flavor
	int[] solution; // The solution
	int[][] A;
	int degree[];
	int n;
	ArrayList<ArrayList<Integer>> solutions = new ArrayList<ArrayList<Integer>>();

	// initialize soveClique with adjacency matrix, it's size, and
	solveClique(int size, int[][] m, int[] degree) {
		this.n = size;
		this.A = m;
		this.degree = degree;
		nodes = maxSize = 0;
		cpuTime = timeLimit = -1;
		style = 1;
		solution = new int[n];
	}

	void search() {
		cpuTime = System.currentTimeMillis();
		nodes = 0;
		ArrayList<Integer> P = new ArrayList<Integer>(n);
		ArrayList<Integer> R = new ArrayList<Integer>();
		ArrayList<Integer> X = new ArrayList<Integer>();
		//Add each vertex index to list P
		for (int i = 0; i < n; i++)
			P.add(i);

		expand(R, P,X);
	}

	ArrayList<Integer> getMaximalClique(){
		int maxSizeIndex = 0;
		int biggestSolution = 0;
		for (int i = 0; i< solutions.size();i++) {
			if (solutions.get(i).size() > biggestSolution) {
				maxSizeIndex = i;
			}
		}
		
		return solutions.get(maxSizeIndex);
	}
	void expand(ArrayList<Integer> R, ArrayList<Integer> P, ArrayList<Integer> X) {
		nodes++;
		//Every node in p gets traversed
		for (int i = P.size() - 1; i >= 0; i--) {
			if (R.size() + P.size() <= maxSize)
				return;
			//get current vertex
			int v = P.get(i);
			//add current vertex to solution set
			R.add(v);
			//create new possible nodes to visit
			ArrayList<Integer> newP = new ArrayList<Integer>();
			//For every vertex in list P
			for (int w : P)
				// Check for connected vertices
				if (A[v][w] == 1)
					//Add them to nodes to be visited
					newP.add(w);
			
			if (newP.isEmpty() && R.size() > maxSize)
				saveSolution(R);
			if (!newP.isEmpty())
				expand(R, newP,X);
			
			R.remove((Integer) v);
			P.remove((Integer) v);
		}
	}

	// Save solution and cliqueSize
	void saveSolution(ArrayList<Integer> C) {
		Arrays.fill(solution, 0);
		for (int i : C) {
			solution[i] = 1;
		}
		maxSize = C.size(); // Set max clique size
	}

	static void display(int[][] matrix) {
		for (int i = 0; i < matrix[0].length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// System.out.println(args[0]);
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

		if (0 < args.length) {
			inFile = new File(args[0]);
		} else {
			System.err.println("Invalid arguments count:" + args.length);
			System.exit(0);
		}

		try {

			// Create new FileReader to read input File
			br = new BufferedReader(new FileReader(inFile));

			System.out.printf("* Max Cliques in graphs in %s\n" + "   (|V|,|E|) Cliques (size, ms used) *", args[0]);
			while ((currentRow = br.readLine()) != null) {
				prevSize = size; // hold size
				if (size == count) {
					size = Integer.parseInt(currentRow);
					if (m != null) {
						graphNumber++;
						sc = new solveClique(prevSize, m, degree);
						sc.search();

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
							m[count][i] = values[i];
							if (values[i] == 1) {
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

