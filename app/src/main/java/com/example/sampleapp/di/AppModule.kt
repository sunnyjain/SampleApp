package com.example.sampleapp.di

import android.app.Application
import androidx.room.Room
import android.content.Context
import com.example.sampleapp.db.Scans
import com.example.sampleapp.db.ScansDao
import com.example.sampleapp.networking.ApiCallInterface
import com.example.sampleapp.networking.MockedInterceptor
import com.example.sampleapp.utils.Constants
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


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


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideMockInterceptor(): Interceptor = MockedInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun getApiCallInterface(retrofit: Retrofit): ApiCallInterface {
        return retrofit.create(ApiCallInterface::class.java!!)
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
