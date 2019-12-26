package com.guness.toptal.client.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guness.toptal.client.ui.entry.EntryViewModel
import com.guness.toptal.client.ui.main.BottomSheetViewModel
import com.guness.toptal.client.ui.main.MainViewModel
import com.guness.toptal.client.utils.viewModel.ViewModelFactory
import com.guness.toptal.client.utils.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    abstract fun bindNewEntryViewModel(viewModel: EntryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetViewModel::class)
    abstract fun bindBottomSheetViewModel(viewModel: BottomSheetViewModel): ViewModel
}