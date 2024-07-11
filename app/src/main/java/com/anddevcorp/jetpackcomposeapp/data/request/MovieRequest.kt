package com.anddevcorp.jetpackcomposeapp.data.request

data class MovieRequest(
    val includeAdult: Boolean = false,
    val includeVideo: Boolean = false,
    val language: String = "tr-TR",
    val page: Int = 1,
    val sortBy: String = "popularity.desc"
)


fun MovieRequest.toMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["include_adult"] = includeAdult.toString()
    map["include_video"] = includeVideo.toString()
    map["language"] = language
    map["page"] = page.toString()
    map["sort_by"] = sortBy
    return map
}
