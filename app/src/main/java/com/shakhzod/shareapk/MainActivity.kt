package com.shakhzod.shareapk

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shakhzod.shareapk.adapter.RvApkAdapter
import com.shakhzod.shareapk.models.Apk
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var apks: MutableList<Apk> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.app_list)

        //Get Apk List
        val pm = applicationContext.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            var name: String?
            if (pm.getApplicationLabel(packageInfo).toString().also { name = it }.isEmpty()) {
                name = packageInfo.packageName
            }
            val icon = pm.getApplicationIcon(packageInfo)
            val apkPath = packageInfo.sourceDir
            val apkSize = File(packageInfo.sourceDir).length()
            apks.add(Apk(name!!, icon, apkPath, apkSize))
        }

        apks.sortWith(Comparator { app, t1 -> app.name.toLowerCase().compareTo(t1.name.toLowerCase()) })

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        val appAdapter = RvApkAdapter(this, apks)
        recyclerView.adapter = appAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.nav_exit)
            finish()
        return true
    }
}