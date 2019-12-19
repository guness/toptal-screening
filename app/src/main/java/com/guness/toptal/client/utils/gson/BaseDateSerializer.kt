package com.guness.toptal.client.utils.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.util.*

abstract class BaseDateSerializer : JsonDeserializer<Date?>, JsonSerializer<Date?> {
    abstract val formatter: DateFormat

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        val dateString = json?.asString ?: return null
        return formatter.parse(dateString)
    }

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        val date = src ?: return null
        val dateString = formatter.format(date)
        return JsonPrimitive(dateString)
    }
}