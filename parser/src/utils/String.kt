package com.suryadigital.eaglegen.parser.utils

fun String.titleCase(): String {
    return this[0].titlecaseChar() + this.substring(1)
}

fun String.kebabCase(): String {
    val stringBuilder = StringBuilder()
    this.forEachIndexed { index, character ->
        if (character.isUpperCase()) {
            if (stringBuilder.isEmpty()) {
                stringBuilder.append(character.lowercase())
            } else {
                if (!((index - 1 >= 0 && this[index - 1].isUpperCase()))) {
                    stringBuilder.append("-")
                    stringBuilder.append(character.lowercase())
                } else if ((index + 2 < length && this[index + 2].isLowerCase() && this[index + 1].isUpperCase())) {
                    stringBuilder.append(character.lowercase())
                    stringBuilder.append("-")
                } else {
                    stringBuilder.append(character.lowercase())
                }
            }
        } else {
            stringBuilder.append(character)
        }
    }
    return "$stringBuilder"
}
