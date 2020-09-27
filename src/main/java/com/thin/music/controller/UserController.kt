package com.thin.music.controller

import com.thin.music.server.UserServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController {

    @Autowired
    private lateinit var server: UserServer
}