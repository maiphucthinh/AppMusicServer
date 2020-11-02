package com.thin.music.model

class ItemChartAlbum {
    var id: String? = null
    var number: String? = null
    var linkImage: String? = null
    var songName: String? = null
    var artistName: String? = null
    var typeMusic: String? = null
    var linkSong: String? = null
    var linkSinger: String? = null
    var linkMusic: String? = null
    var linkAlbum: String? = null
    var albumName: String? = null


    constructor()

    constructor(id: String?, number: String?, linkImage: String?,
                songName: String?, artistName: String?,typeMusic: String?, linkSong: String?,linkSinger: String?) {
        this.id = id
        this.number = number
        this.linkImage = linkImage
        this.songName = songName
        this.artistName = artistName
        this.linkSong = linkSong
        this.typeMusic = typeMusic
        this.linkMusic = linkMusic
        this.linkSinger = linkSinger

    }

    constructor(songName: String?, artistName: String?, linkSong: String?, linkSinger: String?) {
        this.songName = songName
        this.artistName = artistName
        this.linkSong = linkSong
        this.linkSinger = linkSinger
    }

    constructor(id: String?, linkImage: String?, artistName: String?, linkAlbum: String?, albumName: String?) {
        this.id = id
        this.linkImage = linkImage
        this.artistName = artistName
        this.linkAlbum = linkAlbum
        this.albumName = albumName

    }

}