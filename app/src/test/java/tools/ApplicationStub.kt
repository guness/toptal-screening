package tools

import android.Manifest
import com.guness.toptal.client.core.ToptalApplication
import org.robolectric.Shadows.shadowOf
import timber.log.Timber

class ApplicationStub : ToptalApplication() {
    override fun onCreate() {
        super.onCreate()
        shadowOf(this).grantPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        Timber.d("onCreate: $this")
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.d("onTerminate: $this")
    }
}
