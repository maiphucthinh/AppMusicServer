package com.thin.music.controller

import com.thin.music.server.SongSevice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SongApi {
    @Autowired
    private lateinit var service: SongSevice

    @GetMapping("/api/getChart")
    fun getChart(): Any? {
        return service.getAlbum()
    }

    @GetMapping("/api/getLink")
    fun getLinkSong(
            @RequestParam("linkSong", required = false) linkSong: String?
    ): Any? {
        return service.getLinkSong(linkSong)
    }

    @GetMapping("/api/getTheme")
    fun getLinkTheme(
            @RequestParam("linkTheme", required = false)
            linkTheme: String
    ): Any? {
        return service.getChildTheme(linkTheme)
    }

    @GetMapping("/api/searchAll")
    fun allSearch(
            @RequestParam("nameSong", required = false)
            nameSong: String?
    ): Any? {
        return service.getAllSearch(nameSong)
    }

}