package com.thin.music.model

class ItemVideoOnline {
    var id: String? = null
    var linkImage: String? = null
    var linkVideo: String? = null
    var nameVideo: String? = null
    var artistName: String? = null

    constructor(id: String?, linkImage: String?, linkVideo: String?, nameVideo: String?, artistName: String?) {
        this.id = id
        this.linkImage = linkImage
        this.linkVideo = linkVideo
        this.nameVideo = nameVideo
        this.artistName = artistName
    }
}