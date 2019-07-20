package com.example.sampleapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleapp.MainActViewModel
import com.example.sampleapp.ui.history.ScanHistoryViewModel
import com.example.sampleapp.ui.scan.ScannerPageViewModel
import com.example.sampleapp.viewmodel.SampleAppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(ScannerPageViewModel::class)
    abstract fun bindScannerPageViewModel(scannerPageViewModel: ScannerPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanHistoryViewModel::class)
    abstract fun bindScanHistoryViewModel(scannerPageViewModel: ScanHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActViewModel::class)
    abstract fun bindMainActViewModel(mainActViewModel: MainActViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: SampleAppViewModelFactory): ViewModelProvider.Factory

}