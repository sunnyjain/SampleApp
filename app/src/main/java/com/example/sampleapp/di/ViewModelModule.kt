package com.example.sampleapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    abstract fun bindTaskListViewModel(taskListViewModel: ScannerPageViewModel): ViewModel
/*
    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    abstract fun bindAddTaskListViewModel(addTaskViewModel: AddTaskViewModel): ViewModel
*/

    @Binds
    abstract fun bindViewModelFactory(factory: SampleAppViewModelFactory): ViewModelProvider.Factory

}