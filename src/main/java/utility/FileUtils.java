package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());

    public static String readFile(String filename) {
        try {
            BufferedReader buffer = new BufferedReader(
                    new FileReader(filename));

            String contenuto = "", riga;
            riga = buffer.readLine();

            while (riga != null) {
                contenuto += riga + " ";
                riga = buffer.readLine();
            }

            buffer.close();
            return contenuto;

        } catch (FileNotFoundException e) {
            LOG.log(Level.SEVERE, null, e);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }

        return null;
    }

    public static void writeFile(String filename, String content) {
        try {
            BufferedWriter buffer = new BufferedWriter(
                    new FileWriter(filename));

            buffer.write(content);
            buffer.newLine();

            buffer.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
}
