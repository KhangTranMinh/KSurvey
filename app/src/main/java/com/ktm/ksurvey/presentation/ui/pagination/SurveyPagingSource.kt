package com.ktm.ksurvey.presentation.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.usecase.SurveyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurveyPagingSource(
    private val surveyUseCase: SurveyUseCase
) : PagingSource<Int, Survey>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Survey> {
        val pageNumber = params.key ?: 0
        val pageSize = params.loadSize

        return try {
            val result = withContext(Dispatchers.Default) {
                surveyUseCase.getSurveys(pageNumber, pageSize)
            }
            if (result is Result.Success) {
                val surveys = result.data
                LoadResult.Page(
                    data = surveys,
                    prevKey = null,
                    nextKey = if (surveys.isEmpty()) null else (pageNumber + 1)
                )
            } else {
                LoadResult.Error(Exception("Fetch data fail"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Survey>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}