package virtualgameshelf.backend.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TabSeparatedFileReader {
    /** Returns arrayList of lines from a char separated file. (Separator defaults to '\t') */
    public static ArrayList<String[]> readFromFile(String filePath, String separator) {
        File file = new File(filePath);
        String[] array; // Stores a single, split line entry
        ArrayList<String[]> arrayList = new ArrayList<>(); // ArrayList of all entries
        try {
            // http://stackoverflow.com/a/19575418/3357935
            FileReader fileReader = new FileReader(file);
            BufferedReader buf = new BufferedReader(fileReader);
            String lineJustFetched = null;

            while (true) {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null) {
                    // end of file
                    break;
                } else {
                    // '-1' prevents empty entries from being trimmed
                    array = lineJustFetched.split(separator, -1);
                    arrayList.add(array);
                }
            }
            buf.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while reading file " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return arrayList;
    }

    /** Returns arrayList of lines from a char separated file. (Separator defaults to '\t') */
    public static ArrayList<String[]> readFromFile(String filePath, char separator) {
        return readFromFile(filePath, Character.toString(separator));
    }

    /** Returns arrayList of lines from a char separated file. (Separator defaults to '\t') */
    public static ArrayList<String[]> readFromFile(String filePath) {
        return readFromFile(filePath, "\t");
    }

    /** Saves arrayList of lines to a char separated file. (Separator defaults to '\t') */
    public static boolean saveToFile(String filePath, ArrayList<String[]> arrayList, String separator) {
        File file = new File(filePath);
        try {
            FileWriter writer = new FileWriter(file);
            for (String[] s : arrayList) {
                String formattedString = Arrays.toString(s)
                        .replace(", ", separator) // replace commas with separators
                        .replace("[", "")         // remove right bracket
                        .replace("]", "");        // remove left bracket
                writer.write(formattedString + "\r\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file " + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }

        return true;
    }
	
	/** Saves arrayList of lines to a char separated file. (Separator defaults to '\t') */
	public static boolean saveToFile(String filePath, ArrayList<String[]> arrayList, char separator) {
		return saveToFile(filePath, arrayList, Character.toString(separator));
	}
	
	/** Saves arrayList of lines to a char separated file. (Separator defaults to '\t') */
	public static boolean saveToFile(String filePath, ArrayList<String[]> arrayList) {
		return saveToFile(filePath, arrayList, "\t");
	}	
}
