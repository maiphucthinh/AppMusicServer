package com.thin.music.model

class ItemChartAlbum {
    var id: String? = null
    var number: String? = null
    var linkImage: String? = null
    var songName: String? = null
    var artistName: String? = null
    var linkSong: String? = null
    var linkSinger: String? = null
    var linkMusic: String? = null

    constructor()

    constructor(id: String?, number: String?, linkImage: String?,
                songName: String?, artistName: String?, linkSong: String?) {
        this.id = id
        this.number = number
        this.linkImage = linkImage
        this.songName = songName
        this.artistName = artistName
        this.linkSong = linkSong
        this.linkMusic = linkMusic
    }

    constructor(songName: String?, artistName: String?, linkSong: String?, linkSinger: String?) {
        this.songName = songName
        this.artistName = artistName
        this.linkSong = linkSong
        this.linkSinger = linkSinger
    }


}