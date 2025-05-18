package com.anish.gitsearch.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun getUserRepositories(
     //   @Header("Authorization") token: String,
        @Path("user") userName:String,
       // @Query("page") page: Int,
       // @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "stars",
    ): List<RepositoryDto>

    @GET("search/repositories")
    suspend fun searchRepositories(
        //@Header("Authorization") token: String,
        @Query("q") query: String,
       // @Query("page") page: Int,
       // @Query("per_page") perPage: Int
        @Query("sort") sort: String="stars"
    ): SearchResultDto
}