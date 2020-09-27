package com.thin.music.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity(name = "user_profile")
open class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0
    var userName = ""
    var password = ""
    var fullName = ""
    var avatar = ""
    var email = ""

}