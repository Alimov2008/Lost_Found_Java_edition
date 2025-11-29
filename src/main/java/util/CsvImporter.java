package util;

import dao.ItemDao;
import model.Item;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CsvImporter {

    // Helper: split line respecting quotes
    private static String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    public static void importLost() throws Exception {
        importCsv("/csv/lost_database.csv", true);
    }

    public static void importFound() throws Exception {
        importCsv("/csv/found_database.csv", false);
    }

    private static void importCsv(String resourcePath, boolean isLost) throws Exception {
        InputStream in = CsvImporter.class.getResourceAsStream(resourcePath);
        if (in == null) throw new Exception("CSV not found: " + resourcePath);

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        ItemDao dao = new ItemDao();

        br.readLine(); // skip header

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = parseCsvLine(line);

            // remove surrounding quotes
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].replaceAll("^\"|\"$", "");
            }

            Item item = new Item(
                    parts[0],
                    parts[1],
                    Integer.parseInt(parts[2]),
                    parts[3],
                    Integer.parseInt(parts[4]),
                    parts[5],
                    parts[6]
            );

            if (isLost) dao.saveLost(item);
            else dao.saveFound(item);
        }

        System.out.println("Imported: " + resourcePath);
    }
}
