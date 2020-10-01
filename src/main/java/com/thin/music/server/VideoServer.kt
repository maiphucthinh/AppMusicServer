package com.thin.music.server

interface VideoServer {
    fun getVideo(): Any?
    fun getLinkVideo(linkVideo:String):Any
}