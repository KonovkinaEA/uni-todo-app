package com.example.unitodoapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.convertToDateFormat(): String {
    val simpleDateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}