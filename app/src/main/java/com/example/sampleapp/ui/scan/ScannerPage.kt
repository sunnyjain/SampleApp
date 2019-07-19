package com.example.sampleapp.ui.scan


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.camerakit.CameraKitView

import com.example.sampleapp.R
import com.example.sampleapp.di.Injectable
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ScannerPage : Fragment(), Injectable, Detector.Processor<Barcode> {


    override fun release() {

    }

    override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
        Log.e("p0", p0!!.detectedItems[0].displayValue)
    }


    lateinit var rootView: View
    private var scannerSurfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var scannerCamerSource: CameraSource? = null
    private var mlbarcodeDetector: BarcodeDetector? = null
    private var activity: Activity? = null

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
        scannerSurfaceView = view.findViewById(R.id.scanner_surface_view)

    }

    override fun onResume() {
        super.onResume()
        if (scannerCamerSource == null) {
            initSurfaceScanner()
        } else {
            resumeScannerCamera()
        }

    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDetach() {
        super.onDetach()
        if (scannerCamerSource != null) {
            mlbarcodeDetector?.release()
            scannerCamerSource?.release()
            scannerCamerSource = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (hasPermission(rootView.context, Manifest.permission.CAMERA)) {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun useRunTimePermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    fun hasPermission(context: Context, permission: String): Boolean {
        return !useRunTimePermissions() || ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(activity: Activity, permission: Array<String>, requestCode: Int) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode)
        }
    }


    private fun initSurfaceScanner() {
        Log.d("SacnnerPage", "CAMERASTATE: INIT")
        mlbarcodeDetector = BarcodeDetector.Builder(rootView.context).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        mlbarcodeDetector?.setProcessor(this)
        scannerCamerSource = CameraSource.Builder(rootView.context, mlbarcodeDetector)
            .setRequestedPreviewSize(240, 320).setRequestedFps(28.0F)
            .setAutoFocusEnabled(true).build()
        if (surfaceHolder != null) {
            if (ActivityCompat.checkSelfPermission(rootView.context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (activity != null)
                    ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 123)
            } else {
                try {
                    if (scannerCamerSource != null)
                        scannerCamerSource?.start(surfaceHolder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } else {
            scannerSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback2 {
                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
                }


                override fun surfaceCreated(holder: SurfaceHolder) {
                    surfaceHolder = holder
                    if (ActivityCompat.checkSelfPermission(rootView.context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (activity != null)
                            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 123)
                    } else {
                        try {
                            if (scannerCamerSource != null)
                                scannerCamerSource?.start(holder)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    Log.e("surface", "chaneged")
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {

                    if (scannerCamerSource != null) {
                        scannerCamerSource?.stop()

                    }

                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    private fun resumeScannerCamera() {

        val pm = rootView.context.packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //show proper ui to it.
            return
        }
        if (scannerCamerSource != null) {
            try {
                scannerCamerSource?.start(scannerSurfaceView?.holder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            initSurfaceScanner()
        }
    }

    private fun releaseCamera() {
        if (scannerCamerSource != null) {
            scannerCamerSource?.stop()
        }
    }

}
