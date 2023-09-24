package br.com.cardoso.watchcatalog.listener

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.utils.WatchCatalogUtils.Companion.getInfoAndAddWatchFromSharedText

class FabButtonListener(private val context: Context, private val recyclerView: RecyclerView) : OnClickListener {

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
                getInfoAndAddWatchFromSharedText(context, watchLink, recyclerView)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()


    }
}