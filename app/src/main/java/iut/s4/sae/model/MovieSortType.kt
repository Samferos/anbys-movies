package iut.s4.sae.model

enum class MovieSortType(val paramName : String) {
    BY_POPULARITY("popularity"),
    BY_RATING("vote_average"),
    BY_TITLE("title")
}

fun getSortParam(sortType: MovieSortType, ascending: Boolean = false) : String {
    return sortType.paramName + '.' + if (ascending) "asc" else "desc"
}
