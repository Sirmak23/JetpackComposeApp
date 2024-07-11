package com.anddevcorp.jetpackcomposeapp.data.repository

import com.anddevcorp.jetpackcomposeapp.data.request.MovieRequest
import com.anddevcorp.jetpackcomposeapp.model.MovieResponse


interface MovieRepository {
    suspend fun getPopularMovies(request: MovieRequest): MovieResponse
}
