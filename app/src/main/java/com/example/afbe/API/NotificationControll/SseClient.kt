package com.example.afbe.API.NotificationControll

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import com.example.afbe.R
import java.util.concurrent.TimeUnit

class SseClient(private val context: Context) {
    val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()


    fun startListening() {
        val request = Request.Builder()
            .url("https://definite-cobra-diverse.ngrok-free.app/api/sse/subscribe")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SSE", "Error en conexión SSE", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val source = response.body?.source() ?: return
                while (!source.exhausted()) {
                    val line = source.readUtf8Line()
                    if (line != null && line.contains("data:")) {
                        val json = line.substringAfter("data:").trim()
                        val acto = JSONObject(json)
                        val titulo = acto.getString("nombre")
                        mostrarNotificacion(context, "Nuevo acto", titulo)
                    }
                }
            }
        })
    }

    private fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "actos_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Actos", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.clavedesol)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1001, notification)
    }
}
