package com.example.sampleapp.di

import com.example.sampleapp.ui.history.ScanHistory
import com.example.sampleapp.ui.scan.ScannerPage
import dagger.Module
import dagger.android.ContributesAndroidInjector
@Suppress("unused")
@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeTasksListViewFragment(): ScannerPage

    @ContributesAndroidInjector
    abstract fun contributeAddTaskListViewFragment(): ScanHistory

}