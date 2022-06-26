package com.curri.castockapplication.domain.repository

import com.curri.castockapplication.domain.model.CompanyListing
import com.curri.castockapplication.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}