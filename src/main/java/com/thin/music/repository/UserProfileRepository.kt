package com.thin.music.repository

import com.thin.music.model.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
open interface UserProfileRepository : JpaRepository<UserProfile, Int> {
    @Query(nativeQuery = true, value = "SELECT * FROM account WHERE email =:email")
    open fun findOneByEmail(
            @Param("email") email: String
    ): UserProfile?
}