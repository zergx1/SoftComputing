package patternTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConvertPattern {

	private File folder;
	private int[] structure;

	public ConvertPattern(File folder) {
		super();
		this.folder = folder;

		if (folder.listFiles().length > 0) {

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(folder.listFiles()[0]));

				int lines = 0;

				while (br.readLine() != null) {
					lines++;
				}

				br = new BufferedReader(new FileReader(folder.listFiles()[0]));
				int row = br.readLine().length();
				br.close();

				structure = new int[2];
				structure[0] = lines * row;
				structure[1] = folder.listFiles().length;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ConvertPattern(String string) {
		this(new File(string));
	}

	public int[][] getPattern(File file) {
		int[][] matrix = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String strLine;

			int lines = 0;

			while ((strLine = br.readLine()) != null) {
				lines++;
			}

			br = new BufferedReader(new FileReader(file));
			matrix = new int[lines][br.readLine().toCharArray().length];

			br = new BufferedReader(new FileReader(file));
			lines = 0;
			while ((strLine = br.readLine()) != null) {
				char[] chars = strLine.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					matrix[lines][i] = chars[i] != ' '? 1 : 0;
				}
				lines++;
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return matrix;
	}

	public int[] getPatern1D(File file)
	{
		int[][] matrix = getPattern(file);
		
		int[] result = new int[structure[0]];
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
				result[i * matrix[i].length + j] = matrix[i][j];
		}
		return result;
	}
	
	public int[][] getInput()
	{
		int[][] result = new int[structure[1]][structure[0]];
		int i = 0;
		for(File file : folder.listFiles())
		{
			result[i] = getPatern1D(file);
			i++;
		}
		return result;
	}
	
	public double[][] convertIntToDouble(int[][] array)
	{
		double[][] result = new double[array.length][array[0].length];
		
		for(int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < array[i].length; j++)
			{
				result[i][j] = array[i][j];
			}
		}
		
		return result;
	}
	
	public int[][] getOutput()
	{
		int[][] result = new int[structure[1]][structure[1]];
		
		int i = 0;
		for(File file : folder.listFiles())
		{
			int[] temp = new int[structure[1]];
			
			for(int j = 0; j < temp.length; j++)
				temp[j] = i == j? 1 : 0;
			
			result[i] = temp;
			i++;
		}
		return result;
	}
	
	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public int[] getStructure() {
		return structure;
	}

	public void setStructure(int[] structure) {
		this.structure = structure;
	}

}
