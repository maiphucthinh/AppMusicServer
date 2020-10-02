package com.thin.music.model

class ItemSearchOnline {
    var id: String? = null
    var linkImage: String? = null
    var linkAlbums: String? = null
    var nameAlbums: String? = null
    var artistName: String? = null

    constructor(id: String?, linkImage: String?, linkAlbums: String?, nameAlbums: String?, artistName: String?) {
        this.id = id
        this.linkImage = linkImage
        this.linkAlbums = linkAlbums
        this.nameAlbums = nameAlbums
        this.artistName = artistName
    }
}