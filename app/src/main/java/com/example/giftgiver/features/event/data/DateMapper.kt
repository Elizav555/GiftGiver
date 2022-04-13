package com.example.giftgiver.features.event.data

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateMapper {
    private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormatShort: DateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    private val dateFormatLong: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val dateRegex =
        Regex("^(1[0-9]|0[1-9]|3[0-1]|2[0-9]|[1-9]).(0[1-9]|1[0-2]|[1-9]).[1-2][0-9]{3}\$")
    private val todayDate = Calendar.getInstance()

    fun parseStringToCalendar(date: String): Calendar {
        val time = try {
            dateFormat.parse(date)?.time
        } catch (ex: ParseException) {
            dateFormatShort.parse(date)?.time
        }
        time?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return calendar
        }
        return Calendar.getInstance()
    }

    fun formatDateString(date: String?): String {
        if (date.isNullOrBlank()) {
            return ""
        }
        val calendar = Calendar.getInstance()
        return try {
            dateFormatLong.parse(date)?.time?.let { calendar.timeInMillis = it }
            dateFormatLong.format(calendar.time)
        } catch (ex: ParseException) {
            dateFormatShort.parse(date)?.time?.let { calendar.timeInMillis = it }
            dateFormatShort.format(calendar.time)
        }
    }

    fun validateBirthDate(date: String): Boolean {
        if (!date.matches(dateRegex)) {
            return false
        }
        return try {
            val birthDate = dateFormatLong.parse(date)
            if (birthDate.after(todayDate.time)) {
                false
            } else {
                val birthCalendar = Calendar.getInstance()
                birthCalendar.time = birthDate
                todayDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR) <= 100
            }
        } catch (ex: ParseException) {
            false
        }
    }

    fun parseCalendarToString(date: Calendar): String = dateFormat.format(date.time)
}
