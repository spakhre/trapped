package com.trapped.utilities;

import java.io.File;
import java.io.FileWriter;

public class SaveFile {
    String fileName;
    public SaveFile(String fileName)
    {
        this.fileName = fileName;
    }

    public void save(String contents) {
        try
        {
            File file = new File(fileName);
            if(file.createNewFile()==true)
            {
                FileWriter write = new FileWriter(file, false);
                write.write(contents);
                write.close();
                System.out.println("File saved.");
            }
            else
            {
                FileWriter write = new FileWriter(file, true);
                write.write("\n"+contents);
                write.close();
                System.out.println("File saved.");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
