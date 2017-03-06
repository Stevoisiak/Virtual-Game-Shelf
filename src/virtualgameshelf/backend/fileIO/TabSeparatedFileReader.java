package virtualgameshelf.backend.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TabSeparatedFileReader {
    public static void readFromFile(String filePath) {
        File file = new File(filePath);
        try {
            // http://stackoverflow.com/a/19575418/3357935
            FileReader fileReader = new FileReader(file);
            BufferedReader buf = new BufferedReader(fileReader); // TODO: Consistent naming
            String[] array; // Array of a single line
            ArrayList<String[]> arrayList = new ArrayList<>(); // ArrayList of all arrays
            String lineJustFetched = null;

            while (true) {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null) {
                    // end of file
                    break;
                } else {
                    array = lineJustFetched.split("\t");
                    arrayList.add(array);
                }
            }

            // print list of consoles
            for (String[] s : arrayList) {
                System.out.println(Arrays.toString(s));
            }

            buf.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while reading file " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
