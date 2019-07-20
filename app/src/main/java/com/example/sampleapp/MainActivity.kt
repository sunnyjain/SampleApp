package com.example.sampleapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sampleapp.networking.ApiResponse
import com.example.sampleapp.ui.history.ScanHistory
import com.example.sampleapp.ui.scan.ScannerPage
import com.example.sampleapp.viewmodel.SampleAppViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, BottomNavigationView.OnNavigationItemSelectedListener {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    /*fragment instance. letting the activity know which fragment is active*/
    lateinit var fragment: Fragment

    @Inject
    lateinit var viewModelFactory: SampleAppViewModelFactory

    private lateinit var mainActViewModel: MainActViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        mainActViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActViewModel::class.java)
        mainActViewModel.responseLiveData.observe(this, Observer <ApiResponse> {
            mainActViewModel.updateDirtyRecords()
        })
        supportFragmentManager.beginTransaction().add(R.id.mainNavFragment, ScannerPage(), ScannerPage::class.java.simpleName).commit()
    }

    override fun onResume() {
        super.onResume()
        InternetCheck {
            //sends the dirty records if connected to internet.
            if(it)
                mainActViewModel.sendDirtyRecords()
        }

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.nav_scan_history_page) {
            supportFragmentManager.beginTransaction().replace(R.id.mainNavFragment, ScanHistory(), ScanHistory::class.java.simpleName).commit()
            return true
        }
        if(menuItem.itemId == R.id.nav_scan_page) {
            supportFragmentManager.beginTransaction().replace(R.id.mainNavFragment, ScannerPage(), ScannerPage::class.java.simpleName).commit()
            return true
        }
        return false
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }


    /*internet check class. it checks whether your really connected to the internet not connected to WIFI */
    internal class InternetCheck(private val onInternetChecked: (Boolean) -> Unit) :
        AsyncTask<Void, Void, Boolean>() {
        init {
            execute()
        }

        override fun doInBackground(vararg voids: Void): Boolean {
            return try {
                val sock = Socket()
                sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                sock.close()
                true
            } catch (e: IOException) {
                false
            }

        }

        override fun onPostExecute(internet: Boolean) {
            onInternetChecked(internet)
        }
    }
}
