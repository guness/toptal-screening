package com.guness.toptal.client.core

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.guness.toptal.client.R
import com.guness.toptal.client.utils.errors.displayMessage
import com.guness.toptal.client.utils.errors.fresh
import com.guness.toptal.client.utils.errors.handle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel>(private val classType: KClass<VM>, @LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    private val injectTarget = ActivityInjectTarget()

    val viewModelFactory get() = injectTarget.viewModelFactory

    lateinit var viewModel: VM
    protected val disposables = CompositeDisposable()
    protected val foregroundDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.injector.inject(injectTarget)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(classType.java)

        setContentView(layoutInflater.inflate(layoutRes, null, false))

        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            it.setNavigationOnClickListener { onBackPressed() }
        }

        initView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        foregroundDisposables += viewModel.errors
            .filter { it.fresh }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.handle {
                    val message = it.displayMessage.getText(this)!!
                    val rootView = findViewById<View>(R.id.rootView)

                    if (rootView == null) {
                        Toast.makeText(this, message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getColor(R.color.color_secondary_variant))
                            .also { snackbar ->
                                findViewById<FloatingActionButton>(R.id.fab)?.let(snackbar::setAnchorView)
                            }
                            .show()
                    }
                }
            }
    }

    override fun onPause() {
        super.onPause()
        foregroundDisposables.clear()
    }

    override fun onDestroy() {
        disposables.dispose()
        foregroundDisposables.dispose()
        super.onDestroy()
    }

    abstract fun initView()
}

class ActivityInjectTarget {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}