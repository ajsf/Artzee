package com.doublea.artzee.commons.data

import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.network.ArtsyService
import io.reactivex.Single

class ArtRepositoryImpl constructor(private val api: ArtsyService) : ArtRepository {

    override fun getArt(startIndex: Int): Single<List<Art>> = api.getArt()
            .map { it._embedded.artworks.map { it.toArt() } }
}