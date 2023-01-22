package com.salim.mypokedex.profile

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * A data class representing a user profile. Using Room to save info instead of shared preference
 * because want to use this as excuse to write room migration and test those migrations.
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
