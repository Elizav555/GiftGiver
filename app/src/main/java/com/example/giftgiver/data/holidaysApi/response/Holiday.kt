package com.example.giftgiver.data.holidaysApi.response

data class Holiday(
    val counties: Any,
    val countryCode: String,
    val date: String,
    val fixed: Boolean,
    val global: Boolean,
    val launchYear: Any,
    val localName: String,
    val name: String,
    val types: List<String>
)
