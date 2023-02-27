package com.bogdanmurzin.data.repositories

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.bogdanmurzin.domain.entities.LocalMusic
import javax.inject.Inject

class MusicLocalDataSourceImpl @Inject constructor(private val contentResolver: ContentResolver) :
    MusicLocalDataSource {

    override suspend fun getAllMusic(): List<LocalMusic> {
        val songList = mutableListOf<LocalMusic>()
        val musicUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor: Cursor? = contentResolver.query(musicUri, null, null, null, null)

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            val titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val idAlbum = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            //add songs to list
            do {
                val thisId = musicCursor.getLong(idColumn)
                val thisTitle = musicCursor.getString(titleColumn)
                val thisArtist = musicCursor.getString(artistColumn)
                val albumArt = getAlbumArt(idAlbum)
                songList.add(LocalMusic(thisId, thisTitle, thisArtist, albumArt))
            } while (musicCursor.moveToNext())
        }

        musicCursor?.close()

        return songList
    }

    private fun getAlbumArt(albumId: Int): String? {
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        var albumArtUri : String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                albumArtUri = it.getString(0)
            }
        }

        return albumArtUri
    }
}