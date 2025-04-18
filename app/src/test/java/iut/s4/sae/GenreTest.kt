package iut.s4.sae

import iut.s4.sae.model.Genre
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test


class GenreTest {

    private val testGenre = Genre(
        id = 1,
        name = "Action"
    )

    @Test
    fun `test Genre object equality`() {
        // Check equality between the original and copied Genre object
        val copiedGenre = testGenre.copy()
        assertEquals(testGenre, copiedGenre)
    }

    @Test
    fun `test Genre copy functionality`() {
        // Check if the copy function works as expected
        val copiedGenre = testGenre.copy(name = "Adventure")
        assertNotEquals(testGenre.name, copiedGenre.name)
        assertEquals("Adventure", copiedGenre.name)
    }

    @Test
    fun `test JSON serialization and deserialization for Genre`() {
        // Test that a Genre object can be serialized to JSON and deserialized back
        val json = Json.encodeToString(testGenre)
        val parsedGenre = Json.decodeFromString<Genre>(json)

        // Verify that the deserialized object is the same as the original
        assertEquals(testGenre, parsedGenre)
    }

    @Test
    fun `test Genre equality with different objects`() {
        // Test that two different Genre objects with the same ID but different names are not equal
        val anotherGenre = Genre(id = 1, name = "Adventure")
        assertNotEquals(testGenre, anotherGenre)
    }
}
