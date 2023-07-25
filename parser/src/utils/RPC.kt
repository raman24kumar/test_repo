package com.suryadigital.eaglegen.parser.utils

import com.suryadigital.eaglegen.parser.types.RPCTypes
import com.suryadigital.eaglegen.parser.types.Type

fun Type.getRPCType(): RPCTypes {
    return when (this) {
        Type.Int -> {
            RPCTypes.Int32
        }

        Type.String -> {
            RPCTypes.String
        }

        Type.Long -> {
            RPCTypes.Int64
        }

        Type.Amount -> {
            RPCTypes.Int64
        }

        Type.UUID -> {
            RPCTypes.UUID
        }
        Type.Enum -> {
            RPCTypes.Enum
        }
    }
}
