package com.guness.toptal.client.utils.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.guness.toptal.protocol.util.Exclude

class AnnotationExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?) = false

    override fun shouldSkipField(f: FieldAttributes?) = f?.getAnnotation(Exclude::class.java) != null
}