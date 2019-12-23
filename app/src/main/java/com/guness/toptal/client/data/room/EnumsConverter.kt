package com.guness.toptal.client.data.room

import androidx.room.TypeConverter
import com.guness.toptal.client.utils.extensions.serializedName
import com.guness.toptal.protocol.dto.UserRole

object EnumsConverter {

    @TypeConverter
    @JvmStatic
    fun toUserRole(value: String?): UserRole? {
        return stringToEnum(UserRole.values(), value)
    }

    @TypeConverter
    @JvmStatic
    fun fromUserRole(value: UserRole?): String? {
        return enumToString(value)
    }


    private fun enumToString(enum: Enum<*>?): String? {
        return enum?.serializedName()
    }

    private inline fun <reified T : Enum<*>> stringToEnum(values: Array<T>, value: String?): T? {
        return values.findLast { it.serializedName() == value }
    }
}