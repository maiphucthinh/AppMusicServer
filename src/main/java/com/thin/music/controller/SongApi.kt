package com.thin.music.controller

import com.thin.music.server.SongSevice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SongApi {
    @Autowired
    private lateinit var service: SongSevice

    @GetMapping("/api/searchSong")
    fun searchSong(
            @RequestParam("songName", required = false)
            songName: String?
    ): Any? {
        return service.searchSong(songName)
    }

    @GetMapping("/api/getChart")
    fun getChart(): Any? {
        return service.getChart()
    }

}