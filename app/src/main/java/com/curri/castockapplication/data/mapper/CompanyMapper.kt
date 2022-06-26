package com.curri.castockapplication.data.mapper

import com.curri.castockapplication.data.local.CompanyListingEntity
import com.curri.castockapplication.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = this.name,
        symbols = this.symbols,
        exchange = this.exchange
    )
}

//Reverse mapper to save domain to database

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = this.name,
        symbols = this.symbols,
        exchange = this.exchange
    )
}