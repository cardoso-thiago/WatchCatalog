package br.com.cardoso.watchcatalog

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.cardoso.watchcatalog.model.Watch

class WatchDb(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = ("CREATE TABLE $TABLE ($ID integer primary key autoincrement, $WATCH_LINK text, $WATCH_IMAGE_LINK text)")
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(("DROP TABLE IF EXISTS $TABLE"))
        onCreate(db)
    }

    fun addNewWatch(watchLink: String, watchImageLink: String) {
        val values = ContentValues().apply {
            put(WATCH_LINK, watchLink)
            put(WATCH_IMAGE_LINK, watchImageLink)
        }
        this.writableDatabase.insert(TABLE, null, values)
    }

    fun getAllWatches(): MutableList<Watch> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.query(TABLE, null, null, null, null, null, null, null)
        val watches: MutableList<Watch> = ArrayList()
        with(cursor) {
            while (moveToNext()) {
                val watchLink = getString(getColumnIndexOrThrow(WATCH_LINK))
                val watchImageLink = getString(getColumnIndexOrThrow(WATCH_IMAGE_LINK))
                watches.add(Watch(watchLink, watchImageLink))
            }
        }
        cursor.close()
        db.close()
        return watches
    }

    companion object {
        const val DB_NAME = "watch_catalog.db"
        const val TABLE = "watch"
        const val ID = "_id"
        const val WATCH_LINK = "watch_link"
        const val WATCH_IMAGE_LINK = "watch_image_link"
        const val VERSION = 4
    }
}