package com.doublea.artzee.commons.data

import com.doublea.artzee.commons.data.models.Art
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Test

class ArtRepositoryImplTest {

    @Test
    fun sanityCheck() {
        val artRepo = ArtRepositoryImpl()
        val testSub = TestObserver<Art>()
        val response = artRepo.getArt().subscribe()

        println(response)
    }

}