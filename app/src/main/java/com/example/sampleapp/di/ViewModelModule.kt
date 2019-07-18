package com.example.sampleapp.di

import android.arch.lifecycle.ViewModelProvider
import com.example.sampleapp.viewmodel.SampleAppViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

/*
    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    abstract fun bindTaskListViewModel(taskListViewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    abstract fun bindAddTaskListViewModel(addTaskViewModel: AddTaskViewModel): ViewModel
*/

    @Binds
    abstract fun bindViewModelFactory(factory: SampleAppViewModelFactory): ViewModelProvider.Factory

}