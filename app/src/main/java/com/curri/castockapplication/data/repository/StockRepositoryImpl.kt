package com.curri.castockapplication.data.repository

import com.curri.castockapplication.data.csv.CSVParser
import com.curri.castockapplication.data.local.StockDatabase
import com.curri.castockapplication.data.mapper.toCompanyListing
import com.curri.castockapplication.data.mapper.toCompanyListingEntity
import com.curri.castockapplication.data.remote.StockApi
import com.curri.castockapplication.domain.model.CompanyListing
import com.curri.castockapplication.domain.repository.StockRepository
import com.curri.castockapplication.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    private val companyCSVParser: CSVParser<CompanyListing>

) : StockRepository {

    private val dao = db.dao
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localList = dao.searchCompany(query)
            emit(Resource.Success(
                data = localList.map { it.toCompanyListing() }
            ))
            /**
             * The code above is not send the api request
             * it just get the list saved in database and show in ui first
             * If the database is empty -> it will emit the empty list
             */

            val isDbEmpty = localList.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getAllStock(query)
                companyCSVParser.parse(response.byteStream())
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Could not load the data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Http method is error"))
                null
            }

            remoteListing?.let { listing ->
                dao.deleteCompanyListing()
                listing.map {
                    it.toCompanyListingEntity()
                }.let {
                    dao.insertCompanyListing(it)
                }
                emit(Resource.Loading(isLoading = false))
                emit(
                    Resource.Success(
                        data = dao.searchCompany(query = "").map { it.toCompanyListing() })
                )
                dao.deleteCompanyListing()

            }
        }
    }
}