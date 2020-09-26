package com.thin.music.model

class ItemMusicOnline {
    var id: String? = null
    var linkImage: String? = null
    var songName: String? = null
    var artistName: String? = null
    var linkSong: String? = null
    var linkMusic: String? = null

    constructor(id: String?, linkImage: String?, songName: String?,
                artistName: String?, linkSong: String?) {
        this.id = id
        this.linkImage = linkImage
        this.songName = songName
        this.artistName = artistName
        this.linkSong = linkSong
    };
}