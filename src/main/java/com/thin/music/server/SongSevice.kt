package com.thin.music.server

interface SongSevice {
    fun searchSong(name: String?): Any?
    fun getLinkSong(linkSong: String?): Any?
    fun getAlbum(): Any?
    fun searchAlbums(nameAlbum:String?): Any?
    fun getChildTheme(linkTheme:String): Any?



}