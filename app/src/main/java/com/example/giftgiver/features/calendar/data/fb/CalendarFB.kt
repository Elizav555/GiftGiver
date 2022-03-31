package com.example.giftgiver.features.calendar.data.fb

import com.example.giftgiver.features.event.data.fb.EventFB

data class CalendarFB(
    var events: List<EventFB> = listOf(),
)
