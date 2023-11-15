package com.example.asisten_damkar.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityEvaluasiBinding
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.EvaluasiFragmentViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EvaluasiActivity : AppCompatActivity() {
    private lateinit var items: Array<FireLocationResponse>
    private lateinit var binding: ActivityEvaluasiBinding
    private val STORAGE_PERMISSION_CODE = 23
    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    //Android is 11 (R) or above
                    if(Environment.isExternalStorageManager()){
                        //Manage External Storage Permissions Granted
                        toast("onActivityResult: Manage External Storage Permissions Granted")
                    }else{
                        toast("Storage Permissions Denied");
                    }
                }else{
                    toast("Version not supported");
                }
            }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityEvaluasiBinding>(this, R.layout.activity_evaluasi)

        val model = ViewModelProvider(this)[EvaluasiFragmentViewModel::class.java]
        val loginUtils = LoginUtils(this)

        if(!checkPermission()) {
            requestForStoragePermissions()
        }

        val result = model.fetchFirstData(loginUtils.getAccessToken()!!, loginUtils.getPosXid())

        result.observe(this, Observer {
            if(it == null) {
                return@Observer
            }
            items = it.items
            createChart()
        })

        binding.downloadPdf.setOnClickListener {
            createPdf()
        }
    }

    private fun createChart() {
        var clear = 0
        var late = 0

        for(i in items) {
            if(i.status == "Clear") {
                clear++
            } else if(i.status == "Late") {
                late++
            }
        }

        val entries = arrayListOf<PieEntry>()
        entries.add(PieEntry(clear.toFloat(), "Clear"))
        entries.add(PieEntry(late.toFloat(), "Late"))

        val pieDataSet = PieDataSet(entries, "")
        val colors = mutableListOf<Int>()
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.data = pieData
        binding.pieChart.animateY(1000)
        binding.pieChart.invalidate()
    }

    fun checkPermission(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
    }

    fun requestForStoragePermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent);
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if(read && write){
                    toast("Storage Permissions Granted")
                }else{
                    toast("Storage Permissions Denied")
                }
            }
        }
    }


    private fun createPdf() {
        val mDoc = Document()
        val milis = System.currentTimeMillis().toString()

        val mFilePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + milis + ".pdf"
        val formatTime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        try {

            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Asisten Damkar")
            for (i in items) {
                val tanggal = Date(i.createdAt.toLong() * 1000)
                val dateString = formatTime.format(tanggal)
                mDoc.add(Paragraph("xid: ${i.xid} pos: ${i.pos?.name} status: ${i.status} tanggal: ${dateString}"))
            }
            mDoc.close()
            toast("$milis.pdf is create to \n$mFilePath")
        } catch (e: Exception) {
            toast(e.message + e.toString())
        }
    }
}