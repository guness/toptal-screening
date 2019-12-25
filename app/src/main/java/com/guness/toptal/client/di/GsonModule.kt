package com.guness.toptal.client.di

import com.google.gson.*
import com.guness.toptal.client.utils.gson.AnnotationExclusionStrategy
import dagger.Module
import dagger.Provides
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*
import javax.inject.Singleton

@Module
class GsonModule {

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return gson
    }

    companion object {

        val gson by lazy {
            GsonBuilder()
                .registerTypeAdapter(DateTime::class.java, JsonDeserializer<DateTime> { json, _, _ ->
                    val dateTime = DateTime(json.asString, DateTimeZone.UTC)
                    dateTime.withZone(DateTimeZone.getDefault())
                })
                .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
                    val dateTime = DateTime(json.asString, DateTimeZone.UTC)
                    dateTime.withZone(DateTimeZone.getDefault()).toDate()
                })
                .registerTypeAdapter(DateTimeZone::class.java, JsonDeserializer<DateTimeZone> { json, _, _ ->
                    DateTimeZone.forID(json.asString)
                })
                .registerTypeAdapter(DateTimeZone::class.java, JsonSerializer<DateTimeZone> { obj, _, _ ->
                    JsonPrimitive(obj.id)
                })
/*                .registerTypeAdapter(
                    PushNotificationData::class.java,
                    pushNotificationDataDeserializer
                )
                .registerTypeAdapter(
                    ProfilePushNotificationMeta::class.java,
                    profileNotificationMetaDeserializer
                )
                .registerTypeAdapter(NotificationItem::class.java, notificationItemDeserializer)
                .registerTypeAdapter(InAppMessage::class.java, inAppMessageDeserializer)
                .registerTypeAdapter(NotificationAction::class.java, notificationActionDeserializer)
 */
                .setExclusionStrategies(AnnotationExclusionStrategy())
                .create()
        }
    }
}