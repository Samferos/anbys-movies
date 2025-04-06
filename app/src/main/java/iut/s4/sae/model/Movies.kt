package iut.s4.sae.model

import kotlinx.serialization.Serializable

@Serializable
data class Movies(val results: List<Movie>)