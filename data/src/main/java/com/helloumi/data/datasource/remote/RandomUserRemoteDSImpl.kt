package com.helloumi.data.datasource.remote

import com.helloumi.data.datasource.remote.api.RandomUserAPI
import com.helloumi.data.datasource.remote.response.RandomUserResponse
import javax.inject.Inject

class RandomUserRemoteDSImpl @Inject constructor(
    private val randomUserAPI: RandomUserAPI
) : RandomUserRemoteDS {
    override suspend fun getContacts(results: Int, page: Int): RandomUserResponse =
        randomUserAPI.getContacts(results = results, page = page)
}
