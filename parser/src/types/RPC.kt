package com.suryadigital.eaglegen.parser.types

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RPC(
    val name: String,
    val request: List<RequestAndResponseType>?,
    val response: List<RequestAndResponseType>?,
    val requiresAuthentication: String = "NONE", // For now, we are making it so that no authentication is required. This needs to be changed to "CLIENT_TO_SERVER" one auth module is done.
    val errors: List<ErrorType>? = null,
) : Eagle

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ListRPC(
    val name: String,
    val request: List<RequestAndResponseType>?,
    val response: RequestAndResponseType,
    val requiresAuthentication: String = "NONE", // For now, we are making it so that no authentication is required. This needs to be changed to "CLIENT_TO_SERVER" one auth module is done.
    val errors: List<ErrorType>? = null,
) : Eagle

data class ErrorType(
    val code: String,
)

data class TypeFile(
    val name: String,
    val attributes: List<RequestAndResponseType>,
) : Eagle

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequestAndResponseType(
    val name: String,
    val type: String = RPCTypes.List.name,
    val elementType: String? = null,
    val cases: List<Any>? = null,
    val optional: Boolean? = null,
)

data class PackageYml(
    val packageName: String,
    val name: String,
) : Eagle

enum class RPCTypes {
    String,
    Int32,
    Int64,
    List,
    UUID,
    Enum,
}
