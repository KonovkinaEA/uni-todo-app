package com.example.unitodoapp.utils

import com.example.unitodoapp.data.model.Importance
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.convertToDateFormat(): String =
    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this)

fun String.convertToImportance(): Importance =
    when (this) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }
