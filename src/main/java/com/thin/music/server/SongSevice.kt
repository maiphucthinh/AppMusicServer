package com.thin.music.server

interface SongSevice {
    fun getLinkSong(linkSong: String?): Any?
    fun getAlbum(): Any?
    fun getChildTheme(linkTheme: String): Any?
    fun getAllSearch(songName: String?): Any?
    fun getAllArtistSong(linkArtist: String?): Any?


}