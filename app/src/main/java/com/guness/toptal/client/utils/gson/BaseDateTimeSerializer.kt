package com.guness.toptal.client.utils.gson

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import java.lang.reflect.Type

abstract class BaseDateTimeSerializer : JsonDeserializer<DateTime?>, JsonSerializer<DateTime?> {
    abstract val formatter: DateTimeFormatter

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime? {
        val dateString = json?.asString ?: return null
        return formatter.parseDateTime(dateString)
    }

    override fun serialize(src: DateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        val date = src ?: return null
        val dateString = formatter.print(date)
        return JsonPrimitive(dateString)
    }
}