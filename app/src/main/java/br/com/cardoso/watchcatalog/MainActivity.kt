package br.com.cardoso.watchcatalog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cardoso.watchcatalog.model.Watch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)

        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        val watches: MutableList<Watch> = WatchDb(this).getAllWatches()

        val adapter = ImageGalleryAdapter(this)
        adapter.watchLinks = watches.toTypedArray()
        recyclerView.adapter = adapter

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener(FabButtonListener(this, adapter))
    }
}