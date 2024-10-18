data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val fullDescription: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val mapLink: String,
    val rating: Double,
    val type: String
)

data class PlacesResponse(
    val places: List<Place>
)

