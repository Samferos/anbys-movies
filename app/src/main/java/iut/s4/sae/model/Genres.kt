package iut.s4.sae.model

import iut.s4.sae.network.MovieDao
import kotlinx.serialization.Serializable

@Serializable
data class Genres(val genres: List<Genre>)