package com.thin.music.model

class ItemMusicList<T>{
    var name = ""
    var values = mutableListOf<T>()
    constructor(name:String, values:MutableList<T>){
        this.name = name
        this.values = values
    }
    constructor()
}