package com.thin.music.controller

import com.thin.music.server.VideoServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class VideoApi {

    @Autowired
    private lateinit var server: VideoServer

    @GetMapping("/api/getVideo")
    fun getVideo(): Any? {
        return server.getVideo()
    }

}