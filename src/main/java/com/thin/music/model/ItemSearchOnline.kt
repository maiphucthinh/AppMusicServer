package com.thin.music.model

class ItemSearchOnline {
    var id: String? = null
    var linkImage: String? = null
    var linkMusic: String? = null
    var linkArtist: String? = null
    var title: String? = null
    var artistName: String? = null

    constructor(id: String?, linkImage: String?, linkMusic: String?, title: String?, artistName: String?) {
        this.id = id
        this.linkImage = linkImage
        this.linkMusic = linkMusic
        this.title = title
        this.artistName = artistName
    }

    constructor(id: String?, linkArtist: String?, artistName: String?,linkImage: String?) {
        this.id = id
        this.linkArtist = linkArtist
        this.artistName = artistName
        this.linkImage = linkImage
    }


}