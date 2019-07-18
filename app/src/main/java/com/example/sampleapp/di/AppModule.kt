package com.example.sampleapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

   /* @Singleton
    @Provides
    fun provideDb(app: Application): TaskDB {
        return Room
                .databaseBuilder(app, TaskDB::class.java, "tasks.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: TaskDB): TaskDao {
        return db.taskDao()
    }

    @Provides
    fun providesCompositeDisposableBag(): CompositeDisposable{
        return CompositeDisposable()
    }*/
}
