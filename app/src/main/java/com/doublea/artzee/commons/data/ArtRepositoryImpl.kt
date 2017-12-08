package com.doublea.artzee.commons.data

import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.network.ArtRestApi
import io.reactivex.Single

class ArtRepositoryImpl : ArtRepository {

    private val api = ArtRestApi()

    override fun getArt(startIndex: Int): Single<List<Art>> {
        return api.getArt().map { it._embedded.artworks.map { Art(it.title) } }
    }
}