package com.guness.toptal.client.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.guness.toptal.client.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel>(private val classType: Class<VM>, @LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    private val injectTarget = ActivityInjectTarget()

    val viewModelFactory get() = injectTarget.viewModelFactory

    lateinit var viewModel: VM
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.injector.inject(injectTarget)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(classType)

        setContentView(layoutInflater.inflate(layoutRes, null, false))

        findViewById<Toolbar>(R.id.bar)?.let(::setSupportActionBar)
        initView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        disposables += viewModel.errors.subscribe {
            //todo: display error here
        }
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    abstract fun initView()
}

class ActivityInjectTarget {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}