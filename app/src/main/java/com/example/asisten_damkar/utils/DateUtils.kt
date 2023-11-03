package com.example.asisten_damkar.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getStringDate(epoch: Int): String {
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
    val date = Date(epoch.toLong() * 1000)
    return dateFormatter.format(date)
}