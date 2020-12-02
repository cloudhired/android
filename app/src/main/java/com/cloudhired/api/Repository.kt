package com.cloudhired.api

import com.cloudhired.model.ProfessionalProfile
import com.cloudhired.model.ProfessionalSummary
import com.cloudhired.model.UpdateError
import com.google.gson.JsonObject
import org.json.JSONObject

class Repository(private val api: CloudhiredApi) {
    suspend fun fetchProSum(): List<ProfessionalSummary> {
        return api.getProSum().results
    }

    suspend fun fetchProfile(id: String, idType: String): ProfessionalProfile {
        return api.getProfile(id, idType).data
    }

    suspend fun updateMyProfile(id: String, data: JsonObject): UpdateError {
        println("$id, $data")
        return api.updateProfile(id, data).error
    }
}