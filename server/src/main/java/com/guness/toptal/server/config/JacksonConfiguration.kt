package com.guness.toptal.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.guness.toptal.server.utils.serialization.DateTimeZoneDeserializer
import com.guness.toptal.server.utils.serialization.DateTimeZoneSerializer
import org.joda.time.DateTimeZone
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter


@Configuration
class JacksonConfiguration {

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter().apply {
            objectMapper = ObjectMapper().apply {
                registerModule(KotlinModule())
                registerModule(serializationModule)
            }
        }
    }

    private val serializationModule = SimpleModule()
        .addSerializer(DateTimeZone::class.java, DateTimeZoneSerializer())
        .addDeserializer(DateTimeZone::class.java, DateTimeZoneDeserializer())
}