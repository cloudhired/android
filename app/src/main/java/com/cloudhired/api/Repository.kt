package com.cloudhired.api

class Repository(private val api: CloudhiredApi) {
    suspend fun fetchProSum(): List<ProfessionalSummary> {
        return api.getProSum().results
    }
}