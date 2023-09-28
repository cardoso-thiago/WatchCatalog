package br.com.cardoso.watchcatalog.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.R
import br.com.cardoso.watchcatalog.db.WatchDb

class CustomViewHolder(private val context: Context, itemView: View, private val imageGalleryAdapter: ImageGalleryAdapter) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var imageView: ImageView

    init {
        imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        imageView.setOnClickListener(this)
        imageView.setOnLongClickListener(this)
    }

    override fun onClick(view: View) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val watchLink = view.tag as String
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(watchLink)
            context.startActivity(intent)
        }
    }

    override fun onLongClick(view: View): Boolean {
        val position = adapterPosition
        var deleteConfirmation = false
        if (position != RecyclerView.NO_POSITION) {
            val alertDialog: AlertDialog = context.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(R.string.ok) { dialog, _ ->
                        val watchLink = view.tag as String
                        val watchDb = WatchDb(context)
                        watchDb.deleteWatchFromLink(watchLink)
                        val allLinks = watchDb.getAllWatches()
                        imageGalleryAdapter.watchLinks = allLinks.toTypedArray()
                        imageGalleryAdapter.notifyItemRemoved(position)
                        imageGalleryAdapter.notifyItemRangeChanged(position, allLinks.size);
                        deleteConfirmation = true
                        dialog.dismiss()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    setMessage(R.string.delete_message)
                }
                builder.create()
            }
            alertDialog.show()
            return deleteConfirmation
        }
        return false
    }
}