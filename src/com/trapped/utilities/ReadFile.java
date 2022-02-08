package com.trapped.utilities;
import java.io.*;

public class ReadFile {
	private String[] StringArray;
	public ReadFile(String line)//Constructor
	{  
			try 
	    	{   
	        	File file = new File(line);
				FileReader Read = new FileReader(file);
				BufferedReader Reader = new BufferedReader(Read); 
				String ln;
				int num = 0;
				String build = "";
				try {
					while((ln=Reader.readLine())!=null)
					{  
						build+=ln; 
						build+="\n";
						num++; 
					}
					//setStringArray(build.split("\n")); 
					StringArray = build.split("\n");
					Read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}

	public String[] getStringArray() {
		return StringArray;
	}

	public void setStringArray(String[] stringArray) {
		StringArray = stringArray;
	} 
	
	public String toString()//Method  
	{
		String output = "";
		for(String getString : this.StringArray)
		{
			output+=getString;
			output+="\n";
		}
		//return Arrays.toString(StringArray);Print out array on one line
		return output;
	}

}
