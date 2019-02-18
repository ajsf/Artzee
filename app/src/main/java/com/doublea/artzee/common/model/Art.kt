package com.doublea.artzee.common.model

data class Art(
    val id: String,
    val title: String,
    val category: String,
    val medium: String?,
    val date: String?,
    val collectingInstitution: String,
    val image_versions: List<String>,
    val thumbnail: String,
    val image: String,
    val partner: String?,
    val genes: String?,
    val artists: String?,
    val similarArtworks: String?
) {

    fun getImageUrl(imageVersion: String) = this
        .image
        .replace("{image_version}", imageVersion)
}