package com.helloumi.data.datasource.remote.api

import com.helloumi.data.datasource.remote.response.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserAPI {

    /**
     * Gets contacts from RandomUser API with pagination.
     *
     * @param seed the seed for consistent results.
     * @param results the number of results per page.
     * @param page the page number.
     * @return RandomUserResponse
     */
    @GET("api/1.3/")
    suspend fun getContacts(
        @Query("seed") seed: String = "contactapp",
        @Query("results") results: Int = 20,
        @Query("page") page: Int = 1
    ): RandomUserResponse
}
