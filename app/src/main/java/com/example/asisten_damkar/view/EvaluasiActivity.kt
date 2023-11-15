package com.example.asisten_damkar.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
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
    private val STORAGE_CODE = 483481
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityEvaluasiBinding>(this, R.layout.activity_evaluasi)

        val model = ViewModelProvider(this)[EvaluasiFragmentViewModel::class.java]
        val loginUtils = LoginUtils(this)

        val result = model.fetchFirstData(loginUtils.getAccessToken()!!, loginUtils.getPosXid())

        result.observe(this, Observer {
            if(it == null) {
                return@Observer
            }
            items = it.items
            createChart()
        })

        binding.downloadPdf.setOnClickListener {
            checkPermissionAndCreatePdf()
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

    private fun checkPermissionAndCreatePdf() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                return requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_CODE)
            }
        }
        createPdf()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            STORAGE_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPdf()
                } else {
                    toast("Permission denied")
                }
            }
        }
    }

    private fun requestPermission() {
        toast(Environment.getExternalStorageDirectory().toString())
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_CODE)
    }

    private fun createPdf() {
        val mDoc = Document()
        val milis = System.currentTimeMillis().toString()

        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + milis + ".pdf"
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
            toast("$milis.pdf is create to \n$mFilePath")
        } catch (e: Exception) {
            toast(e.message + e.toString())
        }
    }
}