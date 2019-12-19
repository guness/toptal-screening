package com.guness.toptal.client.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.guness.toptal.client.R
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel>(private val classType: Class<VM>, @LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    private val injectTarget = ActivityInjectTarget()

    val viewModelFactory get() = injectTarget.viewModelFactory

    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.injector.inject(injectTarget)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(classType)

        setContentView(layoutInflater.inflate(layoutRes, null, false))

        findViewById<Toolbar>(R.id.toolbar)?.let(::setSupportActionBar)
        initView()
    }

    abstract fun initView()
}

class ActivityInjectTarget {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}