package ense375;

import com.opencsv.CSVReader;

import java.io.FileReader;

/**
 * Reads a CSV file (with header Title,Genres,Year)
 * and calls saveMovie(...) for each line.
 */
public class MovieLoader {
    private final MovieDatabase db;

    public MovieLoader(MovieDatabase db) {
        this.db = db;
    }

    public void loadFromCsv(String pathToCsv) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(pathToCsv))) {
            String[] row;
            reader.readNext();  // skip header
            while ((row = reader.readNext()) != null) {
                String title = row[0];
                String[] gens = row[1].split("\\s*,\\s*");
                int year = Integer.parseInt(row[2]);
                db.saveMovie(new Movie(title, java.util.Arrays.asList(gens), year));
            }
        }
    }
}
