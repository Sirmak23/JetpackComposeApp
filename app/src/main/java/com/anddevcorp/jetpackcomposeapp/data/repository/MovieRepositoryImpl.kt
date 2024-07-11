package com.anddevcorp.jetpackcomposeapp.data.repository

import com.anddevcorp.jetpackcomposeapp.data.request.MovieRequest
import com.anddevcorp.jetpackcomposeapp.data.request.toMap
import com.anddevcorp.jetpackcomposeapp.data.service.MovieService
import com.anddevcorp.jetpackcomposeapp.model.MovieResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
): MovieRepository {

    override suspend fun getPopularMovies(request: MovieRequest): MovieResponse {
        return movieService.getPopularMovies(request.toMap())
    }
}
