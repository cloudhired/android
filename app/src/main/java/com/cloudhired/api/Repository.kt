package com.cloudhired.api

import com.cloudhired.model.ProfessionalProfile
import com.cloudhired.model.ProfessionalSummary

class Repository(private val api: CloudhiredApi) {
    suspend fun fetchProSum(): List<ProfessionalSummary> {
        return api.getProSum().results
    }

    suspend fun fetchProfile(username: String): ProfessionalProfile {
        return api.getProfile(username)
    }
}