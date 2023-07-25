package com.suryadigital.eaglegen.generator.utils

import com.suryadigital.eaglegen.generator.EagleModel
import com.suryadigital.eaglegen.generator.models.GradleModel
import freemarker.template.Configuration
import freemarker.template.Template
import java.io.File
import java.io.StringWriter
import kotlin.io.path.createDirectories

object TemplateConfig {
    val configuration: Configuration = Configuration(Configuration.VERSION_2_3_32)
    init {
        configuration.setClassForTemplateLoading(this.javaClass, "/template")
    }
}

fun renderTemplate(template: Template, dataModel: Any, fileName: String) {
    val writer = StringWriter()
    template.process(dataModel, writer)
    val file = File(fileName)
    file.writeText("$writer")
}

fun generateGradleImpl(dataModel: GradleModel) {
    dataModel.metaModel.folder.createDirectories()
    renderTemplate(
        template = TemplateConfig.configuration.getTemplate(dataModel.metaModel.templatePath),
        dataModel = dataModel,
        fileName = "${dataModel.metaModel.folder}/${dataModel.metaModel.fileName}",
    )
}

fun generateServerImpl(dataModel: EagleModel) {
    dataModel.metaModel.forEach { model ->
        model.folder.createDirectories()
        renderTemplate(
            template = TemplateConfig.configuration.getTemplate(model.templatePath),
            dataModel = dataModel,
            fileName = "${model.folder}/${model.fileName}",
        )
    }
}
