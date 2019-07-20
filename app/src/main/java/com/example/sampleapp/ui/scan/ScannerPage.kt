package com.example.sampleapp.ui.scan


import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sampleapp.R
import com.example.sampleapp.di.Injectable
import com.example.sampleapp.utils.Constants.Companion.CAMERA_INIT_REQUIRED
import com.example.sampleapp.utils.Constants.Companion.CAMERA_PAUSED
import com.example.sampleapp.utils.Constants.Companion.CAMERA_RESUMED
import com.example.sampleapp.utils.Constants.Companion.CAMERA_STARTED
import com.example.sampleapp.utils.Constants.Companion.CAMERA_STOPPED
import com.example.sampleapp.viewmodel.SampleAppViewModelFactory
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ScannerPage : Fragment(), Injectable, ZXingScannerView.ResultHandler {


    @Inject
    lateinit var viewModelFactory: SampleAppViewModelFactory

    private lateinit var scannerPageViewModel: ScannerPageViewModel
    private lateinit var scannedItem: String

    lateinit var rootView: View
    private var activity: Activity? = null
    private lateinit var scannerView: ZXingScannerView
    private val neededPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
    private var handler: Handler? = null
    private var cameraState = CAMERA_INIT_REQUIRED
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        scannerPageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ScannerPageViewModel::class.java)
        scannerPageViewModel.scannedItemLiveData.observe(this, Observer { t ->
            t.let {
                if (it == 0) {
                    if (scannedItem.isNotEmpty() || scannedItem.isNotBlank())
                        scannerPageViewModel.addRecord()
                }
            }
        })
        scannerView = view.findViewById(R.id.scannerView)
        if (checkPermission()) {
            //setupSurfaceHolder()
        }
        if (handler == null)
            handler = Handler()
    }

    override fun onResume() {
        super.onResume()
        if (cameraState == CAMERA_INIT_REQUIRED || cameraState == CAMERA_STOPPED)
            startCamera()
        else
            resumeCamera()
    }

    override fun onPause() {
        super.onPause()
        if (cameraState == CAMERA_STARTED || cameraState == CAMERA_RESUMED)
            pauseCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraState == CAMERA_PAUSED)
            stopCamera()
    }

    //camera methods
    private fun startCamera() {
        cameraState = CAMERA_STARTED
        scannerView.setResultHandler(this)
        handler?.post {
            scannerView.startCamera()
        }
    }

    private fun pauseCamera() {
        cameraState = CAMERA_PAUSED
        handler?.post {
            scannerView.stopCameraPreview()
        }
    }

    private fun resumeCamera() {
        cameraState = CAMERA_RESUMED
        handler?.post {
            scannerView.resumeCameraPreview(this)
        }
    }

    private fun stopCamera() {
        cameraState = CAMERA_STOPPED
        handler?.post {
            scannerView.stopCamera()
        }

    }

    override fun handleResult(rawResult: Result?) {
        Toast.makeText(rootView.context, rawResult?.text, Toast.LENGTH_SHORT).show()
        scannedItem = rawResult?.text ?: ""
        scannerPageViewModel.scannedItem = scannedItem
        scannerPageViewModel.getRecordByScannedItem(scannedItem)

        resumeCamera()
    }


    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            val permissionsNotGranted = ArrayList<String>()
            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(
                        rootView.context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsNotGranted.add(permission)
                }
            }
            if (permissionsNotGranted.size > 0) {
                var shouldShowAlert = false
                for (permission in permissionsNotGranted) {
                    if (activity != null)
                        shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)
                }

                val arr = arrayOfNulls<String>(permissionsNotGranted.size)
                val permissions = permissionsNotGranted.toArray(arr)
                if (shouldShowAlert) {
                    showPermissionAlert(permissions)
                } else {
                    requestPermissions(permissions, REQUEST_CODE)
                }
                return false
            }
        }
        return true
    }


    private fun showPermissionAlert(permissions: Array<String?>) {
        val alertBuilder = AlertDialog.Builder(rootView.context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission Required")
        alertBuilder.setMessage("sdflsnfg")
        alertBuilder.setPositiveButton(android.R.string.yes) { _, _ -> requestPermissions(permissions, REQUEST_CODE) }
        val alert = alertBuilder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(rootView.context, "permissions", Toast.LENGTH_LONG).show()
                        return
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val REQUEST_CODE = 100
    }
}
