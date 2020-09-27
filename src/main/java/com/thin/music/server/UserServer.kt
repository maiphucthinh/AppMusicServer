package com.thin.music.server

import com.thin.music.model.RegisterRequest

interface UserServer {
    fun register(data: RegisterRequest):Any
}