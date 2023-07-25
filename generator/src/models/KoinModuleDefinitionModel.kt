package com.suryadigital.eaglegen.generator.models

import com.suryadigital.eaglegen.generator.EagleModel
import com.suryadigital.eaglegen.generator.MetaModel

data class KoinModuleDefinitionModel(
    val metaInfo: List<MetaModel>,
    val databaseModule: List<Pair<String, String>>,
    val routesModule: List<Pair<String, String>>,
    val packageName: String,
    val folderName: String,

) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}
