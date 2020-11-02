package com.thin.music.server

import com.thin.music.model.RegisterRequest
import com.thin.music.model.entity.UserProfile
import com.thin.music.repository.UserProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserServerImpl : UserService {
    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository
    override fun register(data: RegisterRequest): Any {
        val user = userProfileRepository.findOneByEmail(data.email)
        if (user == null) {
            val newUser = UserProfile()
            newUser.email = data.email
            newUser.password = data.password!!
            userProfileRepository.save(newUser)
            //thanh cong
        } else {
            // khong thafnh cong
        }
        return userProfileRepository
    }
}