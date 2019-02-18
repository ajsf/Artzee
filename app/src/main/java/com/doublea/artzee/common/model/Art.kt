package com.doublea.artzee.common.model

data class Art(
    val id: String,
    val title: String,
    val category: String,
    val medium: String?,
    val date: String?,
    val collectingInstitution: String,
    val thumbnail: String,
    val image: String,
    val imageRectangle: String
)