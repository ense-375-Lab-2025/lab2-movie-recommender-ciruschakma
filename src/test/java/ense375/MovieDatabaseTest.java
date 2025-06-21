package ense375;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieDatabaseTest {
    Connection conn;
    MovieDatabase db;

    @BeforeEach
    void setup() throws Exception {
        // Use a unique database name per test to ensure isolation
        String uniqueDbName = "db" + System.nanoTime();
        conn = DriverManager.getConnection("jdbc:h2:mem:" + uniqueDbName + ";DB_CLOSE_DELAY=-1");
        db = new MovieDatabase(conn);
    }

    @AfterEach
    void teardown() throws Exception {
        conn.close();
    }

    @Test
    void saveAndList_allMoviesShowsIt() throws Exception {
        Movie m = new Movie("Alpha", List.of("Drama"), 2000);
        db.saveMovie(m);

        List<Movie> all = db.allMovies();
        System.out.println("All movies after save: " + all);

        assertEquals(1, all.size());
        assertEquals("Alpha", all.get(0).getTitle());
    }

    @Test
    void removeMovie_deletesIt() throws Exception {
        Movie movie = new Movie("Beta", List.of("Thriller"), 2001);
        db.saveMovie(movie);

        List<Movie> beforeDelete = db.allMovies();
        System.out.println("Before delete: " + beforeDelete);

        // Confirm the movie is present
        assertTrue(beforeDelete.stream().anyMatch(m -> m.getTitle().equals("Beta")),
                   "Beta should exist before deletion.");

        db.removeMovie("Beta");

        List<Movie> afterDelete = db.allMovies();
        System.out.println("After delete: " + afterDelete);

        // Final assertion
        assertTrue(afterDelete.isEmpty(), "Expected the movie list to be empty after deletion.");
    }
}
