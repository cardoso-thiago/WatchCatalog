package br.com.cardoso.watchcatalog.utils

import android.content.Context
import android.util.Log
import android.webkit.WebSettings
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.adapter.ImageGalleryAdapter
import br.com.cardoso.watchcatalog.db.WatchDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class WatchCatalogUtils {

    companion object {
        const val LOG_TAG = "WatchCatalog"

        fun getInfoAndAddWatchFromSharedText(
            context: Context,
            watchLink: String,
            recyclerView: RecyclerView
        ) {
            val regex = Regex("https://play\\.google\\.com\\S+")
            val matches = regex.findAll(watchLink)
            for (match in matches) {
                get(recyclerView, match.value, context)
            }
        }

        private fun get(recyclerView: RecyclerView, watchLink: String, context: Context) {
            val duration = Toast.LENGTH_LONG
            val adapter = recyclerView.adapter as ImageGalleryAdapter
            val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            coroutineScope.launch {
                try {
                    val defaultUserAgent = WebSettings.getDefaultUserAgent(context)
                    val document = Jsoup.connect(watchLink).userAgent(defaultUserAgent).get()
                    val image = document.select("img")[2]
                    val watchImageLink = image.absUrl("src")
                    val watchDb = WatchDb(context)
                    val result = watchDb.addNewWatch(watchLink, watchImageLink)
                    if (result == -1L) {
                        val message = getErrorMessage("Already exists:", watchLink)
                        withContext(Dispatchers.Main) {
                            val toast = Toast.makeText(context, message, duration)
                            toast.show()
                        }
                    }
                    val allLinks = watchDb.getAllWatches()
                    adapter.watchLinks = allLinks.toTypedArray()
                    withContext(Dispatchers.Main) {
                        adapter.notifyItemInserted(allLinks.size)
                    }
                } catch (e: Exception) {
                    val message = getErrorMessage("Error to load data from", watchLink)
                    withContext(Dispatchers.Main) {
                        val toast = Toast.makeText(context, message, duration)
                        toast.show()
                    }
                    Log.e(LOG_TAG, message, e)
                }
            }
        }

        private fun getErrorMessage(errorMessage: String, watchLink: String): String {
            var message = "$errorMessage $watchLink"
            val regex = Regex("id=([^&]+)")
            val watchIdMatch = regex.find(watchLink)
            if (watchIdMatch != null) {
                message = "$errorMessage ${watchIdMatch.groups[1]?.value}"
            }
            return message
        }
    }
}