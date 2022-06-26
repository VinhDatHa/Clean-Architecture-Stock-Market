package com.curri.castockapplication.data.csv

import com.curri.castockapplication.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor() : CSVParser<CompanyListing> {
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        /**
         * CSV file is a table with key - value like the excel.
         * All field of one stock is in a row
         * So the first row is the introduction key like: index - name - symbols ...
         * So we do not need the data of the first row -> we will drop the first row by the line .drop(1)
         */
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val name = line.getOrNull(0)
                    val symbol = line.getOrNull(1)
                    val exchange = line.getOrNull(2)

                    CompanyListing(
                        name = name ?: return@mapNotNull null,
                        symbols = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }
    }

}