package com.example.unitodoapp.ui

import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.datastore.DataStoreManager
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<Repository>(relaxed = true)
    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true)
}
