/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corpus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.json.simple.parser.ParseException;
import utility.Item;
import utility.ItemsReader;
import utility.NELUtils;

/**
 *
 * @author pierpaolo
 */
public class BuildCorpus {

    /**
     * Build the corpus starting from json file and annotate the entities The
     * main requires three arguments: 1) the input file (json) 2) the rho
     * threshold 3) the output file
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 2) {
                ItemsReader reader = new ItemsReader(new File(args[0]));
                reader.open(false);
                double rho = Double.parseDouble(args[1]);
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]));
                int c = 0;
                int i = 0;
                while (reader.hasNext()) {
                    try {
                        Item item = reader.nextItem();
                        i++;
                        String content = (item.getTitle() != null ? item.getTitle() : "") + "\n" + (item.getBody() != null ? item.getBody() : "");
                        String tagmeResult = NELUtils.tagmePOST(content, true);
                        String tokens = NELUtils.processJSONandTokenize(content, tagmeResult, rho, new StandardAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET));
                        writer.append(tokens);
                        writer.newLine();
                        c++;
                        if (c % 1000 == 0) {
                            Logger.getLogger(BuildCorpus.class.getName()).log(Level.INFO, "Processed {0}", c);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(BuildCorpus.class.getName()).log(Level.WARNING, "Skip item " + i, ex);
                    }
                }
                reader.close();
                writer.close();
                Logger.getLogger(BuildCorpus.class.getName()).log(Level.INFO, "Processed {0}", c);
            }
        } catch (IOException ex) {
            Logger.getLogger(BuildCorpus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
