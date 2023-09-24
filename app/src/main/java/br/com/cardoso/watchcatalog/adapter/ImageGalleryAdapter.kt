package br.com.cardoso.watchcatalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.R
import br.com.cardoso.watchcatalog.model.Watch
import com.bumptech.glide.Glide

class ImageGalleryAdapter(
    private val context: Context
) : RecyclerView.Adapter<CustomViewHolder>() {

    lateinit var watchLinks: Array<Watch>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val photoView: View = inflater.inflate(R.layout.item_photo, parent, false)
        return CustomViewHolder(context, photoView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val watchLink: String = watchLinks[position].watchImageLink
        val imageView = holder.imageView
        imageView.tag = watchLinks[position].watchLink
        Glide.with(context)
            .load(watchLink)
            .placeholder(R.drawable.noimage)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return watchLinks.size
    }
}