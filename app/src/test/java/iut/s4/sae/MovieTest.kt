package iut.s4.sae

import iut.s4.sae.model.Genre
import iut.s4.sae.model.Movie
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class MovieTest {

    private val testMovie = Movie(
        adult = false,
        backdropPath = "/path/to/backdrop.jpg",
        genreIds = listOf(28, 12),
        genres = listOf(Genre(1, "Action"), Genre(2, "Adventure")),
        id = 123,
        originalLanguage = "en",
        originalTitle = "Original Title",
        overview = "A great movie!",
        popularity = 99.5,
        posterPath = "/path/to/poster.jpg",
        releaseDate = "2024-12-01",
        title = "Test Movie",
        video = false,
        voteAverage = 8.3,
        voteCount = 500,
        runtime = 120
    )

    @Test
    fun `test equality and copy`() {
        // Check the equality of the original and the copied movie
        val copied = testMovie.copy(title = "Copied Movie")
        assertEquals("Copied Movie", copied.title)
        assertEquals(testMovie.id, copied.id)
        assertNotEquals(testMovie.title, copied.title)
    }

    @Test
    fun `test default values`() {
        // Test default values with minimal setup
        val defaultMovie = Movie(
            adult = false,
            id = 1,
            title = "Default Test"
        )
        assertTrue(defaultMovie.genreIds.isEmpty())
        assertTrue(defaultMovie.genres.isEmpty())
    }

    @Test
    fun `test JSON serialization and deserialization`() {
        // Test serialization to JSON and back to object
        val json = Json.encodeToString(testMovie)
        val parsed = Json.decodeFromString<Movie>(json)
        assertEquals(testMovie, parsed)
    }
}

