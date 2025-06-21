package ense375;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MovieSuggesterTest {
    Connection conn;
    MovieDatabase db;
    MovieSuggester suggester;

    @BeforeEach
    void prep() throws Exception {
        conn      = DriverManager.getConnection("jdbc:h2:mem:sugg;DB_CLOSE_DELAY=-1");
        db        = new MovieDatabase(conn);
        suggester = new MovieSuggester(db);

        db.saveMovie(new Movie("C1", List.of("Action"), 2010));
        db.saveMovie(new Movie("C2", List.of("Action","Drama"), 2011));
        db.saveMovie(new Movie("C3", List.of("Comedy"), 2012));
    }
    @AfterEach
    void done() throws Exception { conn.close(); }

    @Test
    void suggestByGenre_actionFindsTwo() throws Exception {
        List<Movie> res = suggester.suggestByGenre("Action");
        assertEquals(2, res.size());
    }

    @Test
    void suggestByGenre_noneReturnsEmpty() throws Exception {
        assertTrue(suggester.suggestByGenre("Sci-Fi").isEmpty());
    }
}
