package com.example.unitodoapp.utils

import com.example.unitodoapp.data.model.Importance

fun stringToImportance(importance: String): Importance {
    return when (importance) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }
}