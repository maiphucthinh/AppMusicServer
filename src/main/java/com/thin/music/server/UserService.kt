package com.thin.music.server

import com.thin.music.model.RegisterRequest

open interface  UserService {
    fun register(data: RegisterRequest):Any
}