/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author pierpaolo
 */
public class ItemsReader {

    private JsonReader reader;

    private final File file;

    public ItemsReader(File file) {
        this.file = file;
    }

    public void open(boolean compress) throws IOException {
        if (compress) {
            reader = new JsonReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file)), "UTF-8"));
        } else {
            reader = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        }
        reader.beginArray();
    }

    public void open() throws IOException {
        open(false);
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public Item nextItem() throws IOException {
        Item item = new Item();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                reader.beginArray();
                StringBuilder sb = new StringBuilder();
                while (reader.hasNext()) {
                    sb.append(reader.nextString());
                }
                item.setTitle(sb.toString());
                reader.endArray();
            } else if (name.equals("body")) {
                reader.beginArray();
                StringBuilder sb = new StringBuilder();
                while (reader.hasNext()) {
                    sb.append(reader.nextString());
                }
                item.setBody(sb.toString());
                reader.endArray();
            } else if (name.equals("date")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.nextString();
                }
                reader.endArray();
            } else if (name.equals("url")) {
                reader.nextString();
            }
        }
        reader.endObject();
        return item;
    }

    public void close() throws IOException {
        reader.close();
    }

    public static void main(String[] args) {
        try {
            ItemsReader reader = new ItemsReader(new File("/home/pierpaolo/dataset/spectator/processed/SplitByMonth/output/items-1940.json"));
            reader.open(false);
            int c = 0;
            while (reader.hasNext()) {
                System.out.println(reader.nextItem().toString());
                c++;
                if (c % 1000 == 0) {
                    System.out.println(c);
                }
            }
            reader.close();
            System.out.println("Read " + c);
        } catch (IOException ex) {
            Logger.getLogger(ItemsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
