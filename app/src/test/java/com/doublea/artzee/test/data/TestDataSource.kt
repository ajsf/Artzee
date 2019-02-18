package com.doublea.artzee.test.data

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

internal class TestDataSourceFactory<T>(private val list: List<T>) : DataSource.Factory<Int, T>() {
    override fun create(): DataSource<Int, T> {
        return TestDataSource(list)
    }
}

internal class TestDataSource<T>(private val list: List<T>) : PageKeyedDataSource<Int, T>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        callback.onResult(list, null, null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    }
}