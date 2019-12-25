package com.guness.toptal.server.utils.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.joda.time.DateTimeZone
import java.io.IOException
import javax.persistence.AttributeConverter

class TimeZoneSerializer : StdSerializer<DateTimeZone>(DateTimeZone::class.java) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: DateTimeZone, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.id)
    }
}

class TimeZoneDeserializer : StdDeserializer<DateTimeZone>(DateTimeZone::class.java) {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): DateTimeZone? {
        val node: JsonNode = jp.codec.readTree(jp)
        return DateTimeZone.forID(node.textValue())
    }
}

class TimeZoneDBSerializer : AttributeConverter<DateTimeZone, String> {
    override fun convertToDatabaseColumn(attribute: DateTimeZone?): String? {
        return attribute?.id
    }

    override fun convertToEntityAttribute(dbData: String?): DateTimeZone? {
        return dbData?.let(DateTimeZone::forID)
    }
}