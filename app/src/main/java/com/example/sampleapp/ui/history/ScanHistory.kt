package com.example.sampleapp.ui.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.R
import com.example.sampleapp.adapter.ScannedItemListAdapter
import com.example.sampleapp.di.Injectable
import com.example.sampleapp.viewmodel.SampleAppViewModelFactory
import kotlinx.android.synthetic.main.fragment_scan_history.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ScanHistory : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: SampleAppViewModelFactory

    @Inject
    lateinit var adapter: ScannedItemListAdapter

    private lateinit var scanHistoryViewModel: ScanHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemScannedList.layoutManager = LinearLayoutManager(view.context)
        itemScannedList.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scanHistoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(ScanHistoryViewModel::class.java)
        scanHistoryViewModel.scannedItemList.observe(this, Observer {
            adapter.updateList(it)
        })

    }
}
