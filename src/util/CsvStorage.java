package util;

import model.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvStorage {

    public static void save(String filePath, List<Card> cards) throws IOException {
        File file = new File(filePath);

        // make sure folder exists (data/)
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("id,name,set,category,price,quantity");

            for (Card c : cards) {
                out.println(
                        c.getId() + "," +
                        clean(c.getName()) + "," +
                        clean(c.getSetName()) + "," +
                        clean(c.getCategory()) + "," +
                        c.getPrice() + "," +
                        c.getQuantity()
                );
            }
        }
    }

    public static List<Card> load(String filePath) throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return cards;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                cards.add(new Card(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4]),
                        Integer.parseInt(parts[5])
                ));
            }
        }
        return cards;
    }

    // just in case someone types commas
    private static String clean(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }
}
