package virtualgameshelf.backend.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TabSeparatedFileReader {
    public static void readFromFile(String filePath) {
        File file = new File(filePath);
        try {
            // http://stackoverflow.com/a/19575418/3357935
            FileReader fileReader = new FileReader(file);
            BufferedReader buf = new BufferedReader(fileReader); // TODO: Consistent naming
            ArrayList<String> words = new ArrayList<>();
            String lineJustFetched = null;
            String[] wordsArray;

            while (true) {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null) {
                    // end of file
                    break;
                } else {
                    wordsArray = lineJustFetched.split("\t");
                    for (String each : wordsArray) {
                        if (!"".equals(each)) {
                            words.add(each);
                        }
                    }
                }
            }

            // TODO: Debug
            for (String each : words) {
                System.out.println(each);
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
