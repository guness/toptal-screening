package com.guness.toptal.client.data.room

import androidx.room.TypeConverter
import com.guness.toptal.client.utils.extensions.serializedName
import com.guness.toptal.protocol.dto.UserType

object EnumsConverter {

    @TypeConverter
    @JvmStatic
    fun toUserType(value: String?): UserType? {
        return stringToEnum(UserType.values(), value)
    }

    @TypeConverter
    @JvmStatic
    fun fromUserType(value: UserType?): String? {
        return enumToString(value)
    }


    private fun enumToString(enum: Enum<*>?): String? {
        return enum?.serializedName()
    }

    private inline fun <reified T : Enum<*>> stringToEnum(values: Array<T>, value: String?): T? {
        return values.findLast { it.serializedName() == value }
    }
}