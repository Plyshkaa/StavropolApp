data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val image_url: String,
    val map_link: String,
    val rating: Double,
    val type: String
)

data class PlacesResponse(
    val places: List<Place>
)

