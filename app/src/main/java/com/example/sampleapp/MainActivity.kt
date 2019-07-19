package com.example.sampleapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

import com.example.sampleapp.ui.history.ScanHistory
import com.example.sampleapp.ui.scan.ScannerPage
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, BottomNavigationView.OnNavigationItemSelectedListener {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnNavigationItemSelectedListener(this)
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
}
