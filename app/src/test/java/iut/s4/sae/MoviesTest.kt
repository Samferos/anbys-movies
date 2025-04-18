package iut.s4.sae

import iut.s4.sae.model.Genre
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Test

class MoviesTest {

    private val testMovies = Movies(
        results = mutableListOf(
            Movie(
                adult = false,
                backdropPath = "/path/to/backdrop1.jpg",
                genreIds = listOf(28, 12),
                genres = listOf(Genre(1, "Action"), Genre(2, "Adventure")),
                id = 123,
                originalLanguage = "en",
                originalTitle = "Original Title 1",
                overview = "A great action movie!",
                popularity = 99.5,
                posterPath = "/path/to/poster1.jpg",
                releaseDate = "2024-12-01",
                title = "Test Movie 1",
                video = false,
                voteAverage = 8.3,
                voteCount = 500,
                runtime = 120
            ),
            Movie(
                adult = false,
                backdropPath = "/path/to/backdrop2.jpg",
                genreIds = listOf(35, 18),
                genres = listOf(Genre(3, "Comedy"), Genre(4, "Drama")),
                id = 456,
                originalLanguage = "en",
                originalTitle = "Original Title 2",
                overview = "A fantastic comedy movie!",
                popularity = 89.2,
                posterPath = "/path/to/poster2.jpg",
                releaseDate = "2024-11-10",
                title = "Test Movie 2",
                video = false,
                voteAverage = 7.5,
                voteCount = 350,
                runtime = 100
            )
        )
    )

    @Test
    fun `test Movies object equality`() {
        // Ensure the Movies object is correctly compared
        val copiedMovies = testMovies.copy()
        assertEquals(testMovies, copiedMovies)
    }

    @Test
    fun `test Movies list size and content`() {
        // Test that the list contains the correct number of movies and that the titles are correct
        assertEquals(2, testMovies.results.size)
        assertEquals("Test Movie 1", testMovies.results[0].title)
        assertEquals("Test Movie 2", testMovies.results[1].title)
    }

    @Test
    fun `test JSON serialization and deserialization for Movies`() {
        // Test that a Movies object can be serialized to JSON and deserialized back
        val json = Json.encodeToString(testMovies)
        val parsedMovies = Json.decodeFromString<Movies>(json)

        // Verify that the deserialized object is the same as the original
        assertEquals(testMovies, parsedMovies)
    }

    @Test
    fun `test adding and removing movies from Movies`() {
        // Test adding a movie to the list
        val newMovie = Movie(
            adult = false,
            backdropPath = "/path/to/backdrop3.jpg",
            genreIds = listOf(16, 28),
            genres = listOf(Genre(5, "Animation")),
            id = 789,
            originalLanguage = "en",
            originalTitle = "Original Title 3",
            overview = "An animated adventure!",
            popularity = 95.5,
            posterPath = "/path/to/poster3.jpg",
            releaseDate = "2025-01-01",
            title = "Test Movie 3",
            video = false,
            voteAverage = 9.0,
            voteCount = 450,
            runtime = 110
        )

        // Add to the list
        testMovies.results.add(newMovie)
        assertEquals(3, testMovies.results.size)

        // Remove a movie from the list
        testMovies.results.removeAt(0)
        assertEquals(2, testMovies.results.size)
        assertEquals("Test Movie 2", testMovies.results[0].title)
    }
}
