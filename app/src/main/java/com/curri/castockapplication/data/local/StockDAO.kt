package com.curri.castockapplication.data.local

import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface StockDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListing(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM compaylistingentity")
    suspend fun deleteCompanyListing()

    @Query("""
        SELECT * 
        FROM companylistingentity
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR 
        UPPER(:query) == symbol
    """)
    suspend fun searchCompany(query: String): List<CompanyListingEntity>
}