package com.shakhzod.shareapk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.shakhzod.shareapk.BuildConfig
import com.shakhzod.shareapk.R
import com.shakhzod.shareapk.models.Apk
import java.io.File
import kotlin.math.pow


class RvApkAdapter(var context: Context, private var apkList: List<Apk>) :
    RecyclerView.Adapter<RvApkAdapter.AppViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AppViewHolder,
        position: Int
    ) {
        holder.appName.text = apkList[position].name
        val apkSize: Long = apkList[position].apkSize
        holder.apkSize.text = convertToStandartAppSize(apkSize)
        holder.appIcon.setImageDrawable(apkList[position].icon)
        holder.cardView.setOnClickListener {
            val shareAPkIntent = Intent()
            shareAPkIntent.action = Intent.ACTION_SEND
            shareAPkIntent.putExtra(
                Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    File(apkList[position].apkPath)
                )
            )
            shareAPkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareAPkIntent.type = "application/vnd.android.package-archive"
            context.startActivity(Intent.createChooser(shareAPkIntent, "Share APK"))
        }
    }

    private fun convertToStandartAppSize(size: Long): String {
        return when {
            size < 1024 -> {
                String.format(
                        context.getString(R.string.app_size_b),
                        size.toDouble()
                )
            }
            size < 1024.0.pow(2.0) -> {
                String.format(
                        context.getString(R.string.app_size_kib),
                        (size / 1024).toDouble()
                )
            }
            size < 1024.0.pow(3.0) -> {
                String.format(
                        context.getString(R.string.app_size_mib),
                        (size / 1024.0.pow(2.0))
                )
            }
            else -> {
                String.format(
                        context.getString(R.string.app_size_gib),
                        (size / 1024.0.pow(3.0))
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return apkList.size
    }

    class AppViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.app_row)
        var appIcon: ImageView = itemView.findViewById(R.id.app_icon)
        var appName: TextView = itemView.findViewById(R.id.app_name)
        var apkSize: TextView = itemView.findViewById(R.id.apk_size)

    }

}