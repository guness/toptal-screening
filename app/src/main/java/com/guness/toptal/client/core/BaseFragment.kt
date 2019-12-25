package com.guness.toptal.client.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.guness.toptal.client.utils.viewModel.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel>(private val classType: KClass<VM>, @LayoutRes private val layoutRes: Int) : Fragment() {

    private val injectTarget = FragmentInjectTarget()

    val viewModelFactory: ViewModelFactory get() = injectTarget.viewModelFactory

    lateinit var viewModel: VM
    protected val disposables = CompositeDisposable()
    protected val foregroundDisposables = CompositeDisposable()

    protected var savedInstanceState: Bundle? = null


    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        activity!!.application.injector.inject(injectTarget)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(classType.java)
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutRes, container, false)
        this.savedInstanceState = savedInstanceState
        return rootView
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.savedInstanceState = savedInstanceState
        initView()
    }

    override fun onPause() {
        super.onPause()
        foregroundDisposables.clear()
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }


    abstract fun initView()
}

class FragmentInjectTarget {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}