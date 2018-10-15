import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class solve3CNF
{

	public static void main(String[] args)
	{
		Scanner inputStream = null;
		String fileName = args[0];
		ArrayList<Graph> graphList = new ArrayList<Graph>();

		try
		{
			inputStream = new Scanner(new File(fileName));
		}
		catch (Exception e)
		{

		}
		while (inputStream.hasNextLine())
		{
			// int numVar = inputStream.nextInt();
			ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
			matrix.add(new ArrayList<Integer>());
			HashMap<Integer,int[]> clauseIndexGroups = new HashMap<Integer,int[]>();
			// Graph tempGraph = new Graph();

			// System.out.println("Variables = " + numVar);
			// tempGraph.size = numVar * numVar;

			// int[][] matrix = new int[numVar*numVar][numVar*numVar];
			// int currentR = 0;
			// int currentC = 0;

			// BUILD VARIABLE MAP - will fill out later
			String temp = inputStream.nextLine();
			String[] tokens = temp.split(" ");
			int numVar = Integer.parseInt(tokens[0]);
			System.out.println("Tokens length " + tokens.length);
			// int tokenNum = 1;
			matrix.add(new ArrayList<>());
			matrix.get(0).add(0);
			for (int i = 1; i < tokens.length; i++)
			{

				int token = Integer.parseInt(tokens[i]);
				// Add to top column
				matrix.get(0).add(token);
				// Add to row
				matrix.get(matrix.size() - 1).add(token);
				// Add next row
				matrix.add(new ArrayList<>());
			}
			//
			System.out.println("Matrix size minus 2 " + (matrix.size()-2));
			for (int i = 1; i<matrix.size()-1; i+=3){
				int[] tempArray = {i,i+1,i+2};
				clauseIndexGroups.put(i, tempArray);
				clauseIndexGroups.put(i+1, tempArray);
				clauseIndexGroups.put(i+2, tempArray);
			
			}
			
			
			// MAKING CONNECTIONS
			// ROW
			boolean existInGroup = false;
			for (int i = 1; i < matrix.size()-1; i++)
			{
				// COLUMN
				for (int j = 1; j<matrix.size()-1; j++){
					int[] clauseGroup = clauseIndexGroups.get(i);
					for (int k : clauseGroup){
						if (j == k){
							existInGroup = true;
							matrix.get(i).add(0);
							break;
						}
					}
					if (Math.abs(matrix.get(0).get(j)) == Math.abs(matrix.get(i).get(0)) && !existInGroup){
						matrix.get(i).add(0);
						continue;
					}
					if (!existInGroup){
						matrix.get(i).add(1);
						continue;
					}
					existInGroup = false;
				}
			}

			for (ArrayList<Integer> i : matrix)
			{
				for (int j : i)
				{
					System.out.print(j + "\t");
				}
				System.out.println();
			}
			
			
			
			
			
			// matrix.get(0).add(temp);
			// matrix[currentR][0] = temp;
			// matrix[0][currentC] = temp;
			// currentR++;
			// currentC++;

			// for (int i = 0; i < matrix.length; i++)
			// {
			//
			// for (int j = 0; j < matrix.length; j++)
			// {
			// // System.out.println(inputStream.nextInt());
			// matrix[i][j] = inputStream.nextInt();
			// System.out.print(matrix[i][j] + " ");
			// }
			// System.out.println();
			// }

			// tempGraph.setMatrix(matrix);
			// if (tempGraph.size != 0)
			// {
			// graphList.add(tempGraph);
			// }
			// inputStream.nextLine();
		}
	}

}
