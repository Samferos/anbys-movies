package iut.s4.sae.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Genre(
    val id: Int,
    val name: String
) : Parcelable