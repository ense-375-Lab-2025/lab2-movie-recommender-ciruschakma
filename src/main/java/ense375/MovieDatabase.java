package ense375;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple database handler for storing and retrieving movies using JDBC.
 */
public class MovieDatabase {
    private final Connection conn;

    public MovieDatabase(Connection conn) throws SQLException {
        this.conn = conn;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                         " title VARCHAR(255) PRIMARY KEY, " +
                         " genres VARCHAR(255), " +
                         " \"year\" INT)");  // ← FIXED: "year" with quotes
        }
    }

    /** Add or update a movie record */
    public void saveMovie(Movie m) throws SQLException {
        String sql = "MERGE INTO movies(title, genres, \"year\") KEY(title) VALUES (?, ?, ?)";  // ← FIXED
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getTitle());
            ps.setString(2, String.join(",", m.getGenres()));
            ps.setInt(3, m.getYear());
            ps.executeUpdate();
        }
    }

    /** Remove movie by its title */
    public void removeMovie(String title) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
             "DELETE FROM movies WHERE title = ?")) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }

    /** Return every movie in the database */
    public List<Movie> allMovies() throws SQLException {
        List<Movie> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement()
                                .executeQuery("SELECT * FROM movies")) {
            while (rs.next()) {
                list.add(rowToMovie(rs));
            }
        }
        return list;
    }

    /** Return movies whose genre field contains the given word */
    public List<Movie> moviesWithGenre(String genre) throws SQLException {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE genres LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + genre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToMovie(rs));
                }
            }
        }
        return list;
    }

    private Movie rowToMovie(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        List<String> genres = Arrays.asList(rs.getString("genres").split("\\s*,\\s*"));
        int year = rs.getInt("year");  // This can stay as-is, column name is fine here
        return new Movie(title, genres, year);
    }
}
