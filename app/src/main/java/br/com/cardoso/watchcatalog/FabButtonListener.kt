package br.com.cardoso.watchcatalog

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class FabButtonListener(private val context: Context, private val adapter: ImageGalleryAdapter) :
    OnClickListener {

    override fun onClick(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var watchLink: String
        builder.setTitle("Add new watch link")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            run {
                watchLink = input.text.toString()
                try {
                    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                    coroutineScope.launch {
                        val document = Jsoup.connect(watchLink).get()
                        val image = document.select("img")[2]
                        val watchImageLink = image.absUrl("src")

                        val watchDb = WatchDb(context)
                        watchDb.addNewWatch(watchLink, watchImageLink)
                        val allLinks = watchDb.getAllWatches()
                        adapter.watchLinks = allLinks.toTypedArray()
                        withContext(Dispatchers.Main) {
                            adapter.notifyItemInserted(allLinks.size)
                        }
                    }
                } catch (e: IOException) {
                    //TODO tratar erro
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()


    }
}