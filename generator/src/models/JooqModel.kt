package com.suryadigital.eaglegen.generator.models

import com.suryadigital.eaglegen.generator.EagleModel
import com.suryadigital.eaglegen.generator.MetaModel

data class JooqModel(
    val metaInfo: List<MetaModel>,
    val dbHostName: String,
    val dbName: String,
    val packageName: String,
    val dbPassword: String,
    val dbPortNumber: String,
    val dbUser: String,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}
