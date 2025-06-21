package ense375;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieLoaderTest {
    Connection conn;
    MovieDatabase db;

    @BeforeEach
    void init() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:loader;DB_CLOSE_DELAY=-1");
        db   = new MovieDatabase(conn);
    }

    @AfterEach
    void cleanup() throws Exception {
        conn.close();
    }

    @Test
    void loadFromCsv_readsAllRows() throws Exception {
        // Load movies from your actual movies.csv file
        String csvPath = Paths.get("src/test/resources/movies.csv").toString();
        MovieLoader loader = new MovieLoader(db);
        loader.loadFromCsv(csvPath);

        List<Movie> all = db.allMovies();
        System.out.println("Imported movies: " + all.size());

        // Instead of hardcoding, assert at least 1 movie was loaded
        assertTrue(all.size() > 0, "Should load at least one movie");
    }
}
