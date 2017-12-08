package com.doublea.artzee.commons.data

import com.doublea.artzee.commons.data.models.Art
import io.reactivex.Single

interface ArtRepository {

    fun getArt(startIndex: Int = 0): Single<List<Art>>
}