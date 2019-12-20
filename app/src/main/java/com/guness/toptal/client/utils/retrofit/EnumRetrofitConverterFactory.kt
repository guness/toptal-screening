package com.guness.toptal.client.utils.retrofit

import com.guness.toptal.client.utils.extensions.serializedName
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.Type

class EnumRetrofitConverterFactory : Converter.Factory() {
    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<Any, String>? {
        if (type is Class<*> && type.isEnum) {
            return Converter {
                var value = it.toString()
                try {
                    val enum = it as Enum<*>
                    value = enum.serializedName()!!
                } catch (e: Exception) {
                    Timber.e(e)
                }
                value
            }
        }
        return null
    }
}