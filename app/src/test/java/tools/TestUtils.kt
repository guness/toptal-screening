package tools

import androidx.test.core.app.ApplicationProvider
import com.guness.toptal.client.core.ToptalApplication

object TestUtils {
    val application: ToptalApplication
        get() = ApplicationProvider.getApplicationContext()


    var id = System.currentTimeMillis()
        get() {
            field++
            return field
        }
}