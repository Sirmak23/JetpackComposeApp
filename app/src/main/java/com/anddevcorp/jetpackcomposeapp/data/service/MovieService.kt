package com.anddevcorp.jetpackcomposeapp.data.service

import com.anddevcorp.jetpackcomposeapp.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieService {
    @GET("discover/movie")
    suspend fun getPopularMovies(@QueryMap options: Map<String, String>): MovieResponse
}

