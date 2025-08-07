package com.helloumi.data.datasource.remote

import com.helloumi.data.datasource.remote.response.RandomUserResponse

interface RandomUserRemoteDS {

    /**
     * Gets contacts from RandomUser API with pagination.
     *
     * @param results the number of results per page.
     * @param page the page number.
     *
     * @return RandomUserResponse
     */
    suspend fun getContacts(
        results: Int,
        page: Int
    ): RandomUserResponse
}
