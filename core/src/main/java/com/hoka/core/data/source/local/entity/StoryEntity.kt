package com.hoka.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_table")
data class StoryEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,

    @ColumnInfo("photoUrl")
    val photoUrl: String,

    @ColumnInfo("createdAt")
    val createdAt: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("isFavorite")
    var isFavorite: Boolean = false
)