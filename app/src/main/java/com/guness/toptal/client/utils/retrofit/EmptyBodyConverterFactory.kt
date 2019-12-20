package com.guness.toptal.client.utils.retrofit

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EmptyBodyConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> { body -> if (body.contentLength() == 0L) Unit else delegate.convert(body) }
    }
}
