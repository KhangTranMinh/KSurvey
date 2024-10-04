package com.ktm.ksurvey.presentation.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ktm.ksurvey.domain.entity.Survey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class SurveyPagingSource(
//    private val userUseCase: UserUseCase
) : PagingSource<Int, Survey>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Survey> {
        val page = params.key ?: 0

        return try {
            val result = withContext(Dispatchers.Default) {
                // make sure that this action runs in background thread
//                userUseCase.fetchUsers(startUserId, params.loadSize)
                val surveys = arrayListOf<Survey>()
                repeat(5) {
                    val id = UUID.randomUUID().toString()
                    android.util.Log.d("KSurvey", "id: $id")
                    surveys.add(
                        Survey(
                            id = id,
                            coverImageUrl = "https://ucarecdn.com/62886578-df8b-4f65-902e-8e88d97748b8/-/quality/smart_retina/-/format/auto/",
                            title = "Working from home Check-In",
                            desc = "We would like to know how you feel about our work from home"
                        )
                    )
                    surveys.add(
                        Survey(
                            id = id,
                            coverImageUrl = "https://ucarecdn.com/e4bc340d-27e8-4698-80f2-91e0e5c91a13/-/quality/smart_retina/-/format/auto/",
                            title = "Career training and development",
                            desc = "Building a workplace culture that prioritizes belonging and inclusion"
                        )
                    )
                }
                surveys
            }
            LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = if (result.isEmpty()) null else (page + 1)
            )
//            if (result is Result.Success) {
//                val users = result.data.first
//
//                // next fetch action (from local storage or API) will start from the latest ID
//                val lastUserId = result.data.second
//
//                LoadResult.Page(
//                    data = users,
//                    prevKey = null,
//                    nextKey = if (users.isEmpty()) null else lastUserId
//                )
//            } else {
//                LoadResult.Error(Exception("Fetch data fail"))
//            }
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