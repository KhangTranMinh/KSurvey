package com.ktm.ksurvey.data.network

object ApiConfig {

    const val BASE_URL = "https://survey-api.nimblehq.co"
    const val CLIENT_ID = "6GbE8dhoz519l2N_F99StqoOs6Tcmm1rXgda4q__rIw"
    const val CLIENT_SECRET = "_ayfIm7BeUAhx2W1OUqi20fwO3uNxfo1QstyKlFCgHw"

    const val GRANT_TYPE_PASSWORD = "password"
    const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
}

object ErrorCode {
    const val SUCCESS = 0
    const val UNKNOWN = 1
}
