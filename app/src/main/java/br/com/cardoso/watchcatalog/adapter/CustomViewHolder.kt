package br.com.cardoso.watchcatalog.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.R


class CustomViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var imageView: ImageView

    init {
        imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        imageView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val url = view.tag as String
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }
}