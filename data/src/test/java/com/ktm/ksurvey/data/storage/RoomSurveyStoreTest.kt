package com.ktm.ksurvey.data.storage

import com.ktm.ksurvey.data.storage.room.RoomSurveyStore
import com.ktm.ksurvey.data.storage.room.db.SurveyDao
import com.ktm.ksurvey.data.storage.room.model.SurveyDbModel
import com.ktm.ksurvey.domain.entity.Survey
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class RoomSurveyStoreTest {

    @MockK
    lateinit var surveyDao: SurveyDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getSurveys() {
        runTest {
            // mock
            coEvery {
                surveyDao.getSurveys(any(), any(), any())
            } returns arrayListOf(SurveyDbModel(id = ""))

            val result = RoomSurveyStore(surveyDao).getSurveys(anyString(), anyInt(), anyInt())
            assertEquals(1, result.size)
        }
    }

    @Test
    fun saveSurveys() {
        runTest {
            // mock
            coEvery { surveyDao.insertAll(*anyVararg()) } returns Unit

            RoomSurveyStore(surveyDao).saveSurveys(arrayListOf(Survey()))
            coVerify(exactly = 1) { surveyDao.insertAll(*anyVararg()) }
        }
    }

    @Test
    fun delete() {
        runTest {
            // mock
            coEvery { surveyDao.delete() } just runs

            RoomSurveyStore(surveyDao).delete()
            coVerify(exactly = 1) { surveyDao.delete() }
        }
    }
}