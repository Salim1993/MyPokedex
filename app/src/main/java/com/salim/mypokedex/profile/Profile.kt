package com.salim.mypokedex.profile

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * A data class representing a user profile.
 *
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property avatarImageLocation The location of the user's avatar image.
 */
@Entity(tableName = "Profile")
data class Profile(
    @PrimaryKey
    val name: String,
    val email: String,
    val avatarImageLocation: String
)
