package iut.s4.sae

import iut.s4.sae.model.Genre
import iut.s4.sae.model.Genres
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Test

class GenresTest {

    private val testGenres = Genres(
        genres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Comedy"),
            Genre(id = 3, name = "Drama")
        )
    )

    @Test
    fun `test Genres object equality`() {
        // Ensure that the original and copied Genres objects are equal
        val copiedGenres = testGenres.copy()
        assertEquals(testGenres, copiedGenres)
    }

    @Test
    fun `test Genres list size and content`() {
        // Verify the size of the genres list and check content
        assertEquals(3, testGenres.genres.size)
        assertEquals("Action", testGenres.genres[0].name)
        assertEquals("Comedy", testGenres.genres[1].name)
        assertEquals("Drama", testGenres.genres[2].name)
    }

    @Test
    fun `test JSON serialization and deserialization for Genres`() {
        // Test that a Genres object can be serialized to JSON and deserialized back
        val json = Json.encodeToString(testGenres)
        val parsedGenres = Json.decodeFromString<Genres>(json)

        // Verify that the deserialized object is the same as the original
        assertEquals(testGenres, parsedGenres)
    }

    @Test
    fun `test adding genres to Genres`() {
        // Test adding a new genre to the genres list
        val newGenre = Genre(id = 4, name = "Horror")
        val updatedGenres = testGenres.copy(genres = testGenres.genres + newGenre)

        // Ensure the new genre has been added
        assertEquals(4, updatedGenres.genres.size)
        assertEquals("Horror", updatedGenres.genres[3].name)
    }

    @Test
    fun `test removing genres from Genres`() {
        // Test removing a genre from the genres list
        val updatedGenres = testGenres.copy(genres = testGenres.genres - testGenres.genres[1])

        // Ensure the genre has been removed
        assertEquals(2, updatedGenres.genres.size)
        assertEquals("Action", updatedGenres.genres[0].name)
        assertEquals("Drama", updatedGenres.genres[1].name)
    }
}
