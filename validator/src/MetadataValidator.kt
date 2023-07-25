package com.suryadigital.eaglegen.validator

import com.suryadigital.eaglegen.parser.types.*
import com.suryadigital.eaglegen.parser.utils.kebabCase
import java.io.File
import java.nio.file.Path

class MetadataValidator(val filepath: Path, val crud: CRUD) {
    val file: File
        get() = filepath.toFile()

    fun getFields(field: Field, crud: CRUD): DataModelField {
        val fieldType: DataModelType?
        if (field.type == null) {
            require(field.method != null) {
                "The field type and method both cannot be null for the given field ${field.name}"
            }
            fieldType = getType(crud.database.fields.find { it.name == field.name }!!.reference?.key!!.type,null)
        }
        else {
            fieldType = getType(field.type!!,field)
        }

        return DataModelField(
            name = field.name,
            type = fieldType,
            method = field.method?.let {
                when (it) {
                    Method.Lookup -> {
                        DataModelMethod.Lookup(
                            reference = crud.database.fields.find { it.name == field.name }?.let { dbFields ->
                                dbFields.reference?.let { referece ->
                                    if (referece.key.type == Type.Enum || referece.value.type == Type.Enum) {
                                        throw IllegalArgumentException("The type cannot be enum for the given field ${it.name} in the database section ")
                                    }
                                    DataModelMethod.Reference(
                                        tableName = referece.table,
                                        key = DataModelMethod.Attribute(
                                            name = referece.key.column,
                                            type = getType(referece.key.type, field)
                                        ),
                                        label = DataModelMethod.Attribute(
                                            name = referece.value.column,
                                            type = getType(referece.value.type, field)
                                        )
                                    )
                                }
                                    ?: throw IllegalArgumentException("The reference cannot be null for the given field ${field.name}")
                            }
                                ?: throw IllegalArgumentException("The reference cannot be null for the given field ${field.name}"),
                            dependsOn = crud.database.fields.find { it.name == field.name }?.dependsOn?.let {
                                DataModelMethod.DependsOn(
                                    field = getFields(
                                        crud = crud,
                                        field = crud.fields.find { field: Field -> it.field == field.name }
                                            ?: throw IllegalArgumentException("The field ${it.field} is not defined in the fields section")
                                    ),
                                    column = it.column
                                )
                            }
                        )
                    }
                }
            },
            optional = crud.fields.find { it.name == field.name }?.optional ?: false,
            columnToSave = crud.database.fields.find { it.name == field.name }?.column
                ?: if (crud.database.tableToSave.primaryKeyColumn == field.name) crud.database.tableToSave.primaryKeyColumn else null
                    ?: throw IllegalArgumentException("The column was not found in the database section for the given field ${field.name} "),
            isPrimaryKey = crud.database.tableToSave.primaryKeyColumn == field.name,
            isFilterable = crud.webUI.table.filterableAttributes.any { it.name == field.name },
            isFormField = crud.webUI.form.any { it.name == field.name },
            isMandatory = crud.webUI.table.columns.any { it.name == field.name },
            isSearchable = crud.webUI.table.searchableAttributes.any { it.name == field.name },
            title = crud.webUI.textMap.find { it.name == field.name }!!.title
        )
    }

    fun getType(type: Type, field: Field?): DataModelType {
        return when (type) {
            Type.Int -> {
                DataModelType.Int32
            }

            Type.String -> {
                DataModelType.String
            }

            Type.Long -> {
                DataModelType.Int64
            }

            Type.Amount -> {
                throw IllegalStateException("Not handled yet")
            }

            Type.UUID -> {
                DataModelType.UUID
            }

            Type.Enum -> {
                DataModelType.Enum(
                    cases = field!!.options?.map(Attribute::name)
                        ?: throw IllegalArgumentException("The cases should not be null for the type enum for the given name ${field.name}")
                )
            }
        }
    }

    fun generateDataModel(): DataModelCrud {
        validateCRUDMetadata()
        return DataModelCrud(
            crudName = crud.name,
            tableToSave = TableToSave(tableName = crud.database.tableToSave.table, primaryKeyColumn = crud.database.tableToSave.primaryKeyColumn),
            fields = crud.fields.map {
                getFields(it, crud)
            }
        )
    }


    private fun validateCRUDMetadata() {
        validateFileName(crud)
        validatePrimaryKeyDefinition(crud)
        validateFieldsNames(crud)
        validateFieldsWithDBFields(crud)
        validateFieldsTypeAndMethod(crud)
        validateLookupFields(crud)
        validateDependsOnFields(crud)
        validateEnumFields(crud)
        validateUniqueFields(crud)
        validateColumnAndTableNames(crud)
        validateSearchableAttributes(crud) // should include the validation for its name as well as the defintion should exist in the DB as well
        validateFilterableAttributes(crud) // same as above
        validateItemsPerPage(crud)
        validateColumnsInTableSection(crud) // should contain the searchable and filterable attributes combined
        validateFormAttributes(crud)
        validateTextMap(crud)
    }

    private fun validateUniqueFields(crud: CRUD) {
        crud.fields.forEach { field ->
            if (crud.fields.count { field.name == it.name } > 1) {
                throw IllegalArgumentException("The field name '${field.name}' is occurring more than once in the metadata file '${file.name}'.")
            }
        }

        crud.database.fields.forEach { dbField ->
            if (crud.database.fields.count { dbField.name == it.name } > 1) {
                throw IllegalArgumentException("The database field name '${dbField.name}' is occurring more than once in the metadata file '${file.name}'.")
            }
        }
    }

    private fun validateFieldsWithDBFields(crud: CRUD) {
        crud.database.fields.find { dbField ->
            crud.fields.none {
                it.name == dbField.name
            }
        }?.let {
            throw IllegalArgumentException(
                "The Database field '${it.name}' is not defined in the fields section in the crud metadata file '${file.name}'."
            )
        }
    }

    private fun validateFileName(crud: CRUD) {
        val validFileName = crud.name.kebabCase() + "-crud.yml"
        require(file.name == validFileName) {
            "The file name '${file.name}' is not as expected from the definition of CRUD metadata in the file '${validFileName}'."
        }
    }

    private fun validateFieldsNames(crud: CRUD) {
        crud.fields.find { it.name[0].isUpperCase() }
            ?.let { throw IllegalArgumentException("The field name '${it.name}' does not start with lower case in the crud metadata file '${file.name}'.") }
        crud.fields.forEach { it ->
            run {
                require(it.name.matches("^[a-zA-Z0-9]*$".toRegex())) { "Invalid field name '${it.name}' in crud metadata file ${file.name}, the field name cannot contain special characters and whitespaces." }
                if (it.name.contains('_')) {
                    println("The table name '$it.name' in the crud metadata file ${file.name} should not contain underscore character")
                }
            }
        }
    }

    private fun validatePrimaryKeyDefinition(crud: CRUD) {
        val primaryKeyColumn = crud.database.tableToSave.primaryKeyColumn
        val primaryKeyDefinition = crud.fields
            .find { it.name == primaryKeyColumn }
        require(primaryKeyDefinition != null) { "The definition for the primary key '$primaryKeyColumn' is missing in the fields in the crud metadata file '${file.name}'." }
        require(primaryKeyDefinition.type == Type.UUID)
        require(primaryKeyDefinition.optional == null) { "The primary key '${primaryKeyDefinition.name}' cannot be declared as optional in the crud metadata file '${file.name}'." }
    }

    private fun validateDependsOnFields(crud: CRUD) {
        crud.database.fields
            .map(DBFields::dependsOn)
            .forEach { dependsOnField ->
                dependsOnField?.let {
                    require(crud.database.fields.any { it.name == dependsOnField.field }) { "Exception occurred while parsing metadata from file: $file. The field '${dependsOnField.field}' is not defined in the database in the crud metadata file '${file.name}'." }
                }
            }
    }

    private fun validateFieldsTypeAndMethod(crud: CRUD) {
        val fieldWithoutTypeAndMethod = crud.fields.find { it.type == null && it.method == null }
        require(fieldWithoutTypeAndMethod == null) {
            "Exception occurred while parsing metadata from file: ${file.name} The method and the type are missing for the field '${fieldWithoutTypeAndMethod?.name}' in the crud metadata file '${file.name}'."
        }
        val fieldWithTypeAndMethod = crud.fields.find { !(it.type == null || it.method == null) }
        require(fieldWithTypeAndMethod == null) {
            "Exception occurred while parsing metadata from file: ${file.name} The method and the type are both defined for the field '${fieldWithTypeAndMethod?.name}' in the crud metadata file '${file.name}'."
        }
    }

    private fun validateLookupFields(crud: CRUD) {
        for (field in crud.fields) {
            val dbField = crud.database.fields.find { it.name == field.name }
            field.method?.let {
                require(dbField?.reference != null) {
                    "Exception occurred while parsing metadata from file: $file. The reference field is missing for the database field '${field.name}' in the crud metadata file '${file.name}'."
                }
            }
        }
    }

    private fun validateEnumFields(crud: CRUD) {
        crud.fields.filter { it.type == Type.Enum }.forEach { it ->
            require(it.options != null) {
                "The options are not included for the field '${it.name}' of type Enum in the crud metadata file '${file.name}'."
            }
        }
    }

    private fun validateTableName(tableName: String) {
        require(tableName[0].isUpperCase()) { "Invalid table name '$tableName' in crud file ${file.name}, table name must start with capital case." }
        require(tableName.matches("^[a-zA-Z0-9]*$".toRegex())) { "Invalid table name '$tableName' in crud metadata file ${file.name}, the table name cannot contain special characters and whitespaces." }
        if (tableName.contains('_')) {
            println("The table name '$tableName' in the crud metadata file ${file.name} should not contain underscore character")
        }
    }

    private fun validateColumnName(columnName: String) {
        require(columnName[0].isLowerCase()) { "Invalid column name '$columnName' in crud file ${file.name}, column name must start with small letter." }
        require(columnName.matches("^[a-zA-Z0-9]*$".toRegex())) { "Invalid column name '$columnName' in crud metadata file ${file.name}, the column name cannot contain special characters and whitespaces." }
        if (columnName.contains('_')) {
            println("The column name '$columnName' in the crud metadata file ${file.name} should not contain underscore character")
        }
    }

    private fun validateColumnAndTableNames(crud: CRUD) {
        crud.database.fields.forEach { validateColumnName(it.name) }
        crud.database.fields.forEach { it -> it.reference?.let { validateTableName(it.table) } }
        crud.database.fields.forEach { it -> it.reference?.let { validateColumnName(it.key.column) } }
    }

    private fun validateFormAttributes(crud: CRUD) {
        crud.webUI.form.forEach { formAttribute -> require(crud.fields.any { it.name ==  formAttribute.name}) { "The form attribute ${formAttribute.name} is not defined in the fields section in crud metadata file: ${file.name}" } }
    }
    private fun validateFilterableAttributes(crud: CRUD) {
        crud.webUI.table.filterableAttributes.forEach { filterableAttribute -> require(crud.webUI.table.columns.any { it.name ==  filterableAttribute.name}) { "The filterable attribute ${filterableAttribute.name} is not defined in the fields section in crud metadata file: ${file.name}" } }
    }

    private fun validateSearchableAttributes(crud: CRUD) {
        crud.webUI.table.searchableAttributes.forEach { searchableAttribute -> require(crud.webUI.table.columns.any { it.name ==  searchableAttribute.name}) { "The searchable attribute ${searchableAttribute.name} is not defined in the fields section in crud metadata file: ${file.name}" } }
    }

    private fun validateTextMap(crud: CRUD) {
        crud.webUI.textMap.forEach { textMapField ->
            run {
                require(crud.fields.any { textMapField.name == it.name }) { "The textmap field '$textMapField.name' is not defined in the fields section in the crud metadata file ${file.name}." }
                textMapField.title.asSequence().forEach { require((it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '_' || it == ' ')) { "Textmap field title '${textMapField.title}' cannot contain special characters in the crud metadata file ${file.name}" } }
                require(textMapField.title[0].isUpperCase()) { "The fieldmap title '${textMapField.title}' does not begin with capital letter for field '${textMapField.name}' in the crud metadata file ${file.name}." }
            }
        }
    }

    private fun validateItemsPerPage(crud: CRUD) {
        require(crud.webUI.table.itemsPerPage in 1..200) {"The items per page are out of bounds in the crud metadata file ${file.name}, the value must lie in the range [1, 200]."}
    }

    private fun validateColumnsInTableSection(crud: CRUD) {
        crud.webUI.table.columns.forEach { column -> require(crud.webUI.table.columns.any { it.name ==  column.name}) { "The column ${column.name} in tables in WebUI is not defined in the fields section in the crud metadata file: ${file.name}" } }
        require(crud.webUI.table.columns.size == crud.webUI.table.searchableAttributes.size + crud.webUI.table.filterableAttributes.size) {
            "The columns in the table in webUI section contains more columns than expected in the crud metadata file ${file.name}."
        }
    }
}
