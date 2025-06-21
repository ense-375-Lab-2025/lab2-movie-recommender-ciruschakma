package ense375;

import java.util.List;

/**
 * Wraps MovieDatabase to suggest by genre.
 */
public class MovieSuggester {
    private final MovieDatabase db;

    public MovieSuggester(MovieDatabase db) {
        this.db = db;
    }

    /**
     * Returns all movies tagged with the given genre.
     */
    public List<Movie> suggestByGenre(String genre) throws Exception {
        return db.moviesWithGenre(genre);
    }
}
