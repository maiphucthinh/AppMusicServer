package com.thin.music.model

class GetLinkMusic {
    var link = ""
    var lyric = ""
    var songName = ""
    var artistName = ""
    var linkArtist = ""
    var authorName = ""
    var linkAuthor = ""

    constructor(link: String, lyric: String?, songName: String, artistName: String, linkArtist: String, authorName: String?, linkAuthor: String) {
        this.link = link
        if (lyric != null) {
            this.lyric = lyric
        }
        this.songName = songName
        this.artistName = artistName
        this.linkArtist = linkArtist
        if (authorName != null) {
            this.authorName = authorName
        }
        this.linkAuthor = linkAuthor
    }
}