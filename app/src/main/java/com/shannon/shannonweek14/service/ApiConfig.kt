package com.shannon.shannonweek14.service

import SongsService

object ApiConfig {

    fun auth(): AuthService {
        return ApiClient.getClient().create(AuthService::class.java)
    }

    fun journal(token: String): JournalService {
        return ApiClient.getClient(token).create(JournalService::class.java)
    }

    fun songs(token: String): SongsService {
        return ApiClient.getClient(token).create(SongsService::class.java)
    }
}
