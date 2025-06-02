import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

object FileDownloader {

    fun downloadFile(context: Context, url: String, filename: String) {
        val client = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {

                e.printStackTrace()
                showToast(context, "Error al descargar archivo")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    showToast(context, "Error al descargar archivo")
                    return
                }

                val inputStream = response.body?.byteStream()
                if (inputStream == null) {
                    showToast(context, "Archivo vacío")
                    return
                }

                try {
                    val savedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveFileToDownloadsScopedStorage(context, filename, inputStream)
                    } else {

                        saveFileToDownloadsLegacy(context, filename, inputStream)
                    }

                    if (savedUri != null) {
                        showToast(context, "Archivo descargado en Descargas")
                    } else {
                        showToast(context, "Error al guardar archivo")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast(context, "Error al guardar archivo")
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileToDownloadsScopedStorage(context: Context, filename: String, inputStream: java.io.InputStream): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return uri
    }

    private fun saveFileToDownloadsLegacy(context: Context, filename: String, inputStream: java.io.InputStream): Uri? {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsPath.exists()) {
            downloadsPath.mkdirs()
        }
        val file = File(downloadsPath, filename)
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        context.sendBroadcast(
            android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))
        )
        return Uri.fromFile(file)
    }

    private fun showToast(context: Context, msg: String) {
        android.os.Handler(context.mainLooper).post {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
