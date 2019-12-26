package com.guness.toptal.client.core

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.guness.toptal.client.R
import com.guness.toptal.client.utils.errors.fresh
import com.guness.toptal.client.utils.errors.handle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber
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
                    //todo: display error here
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                    Timber.e(it)
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