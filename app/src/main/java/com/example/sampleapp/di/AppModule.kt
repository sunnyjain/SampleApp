package com.example.sampleapp.di

import android.app.Application
import androidx.room.Room
import android.content.Context
import com.example.sampleapp.db.Scans
import com.example.sampleapp.db.ScansDao
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

   @Singleton
    @Provides
    fun provideDb(app: Application): Scans {
        return Room
                .databaseBuilder(app, Scans::class.java, "Scans.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: Scans): ScansDao {
        return db.scansDao()
    }

    @Provides
    fun providesCompositeDisposableBag(): CompositeDisposable{
        return CompositeDisposable()
    }
}
