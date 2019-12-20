package com.guness.toptal.client.data.interceptors

import android.content.Intent
import com.guness.toptal.client.data.handlers.CleanSessionHandler
import com.guness.toptal.client.ui.start.StartActivity
import com.guness.toptal.client.utils.AppContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedHandler @Inject constructor(val context: AppContext, private val cleanSessionHandler: CleanSessionHandler) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val intent = Intent(context, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        cleanSessionHandler.clean()
        context.startActivity(intent)

        return chain.proceed(chain.request())
    }
}