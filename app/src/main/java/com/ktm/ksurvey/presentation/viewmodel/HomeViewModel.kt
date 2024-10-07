package com.ktm.ksurvey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.presentation.ui.pagination.SurveyPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _surveyState: MutableStateFlow<PagingData<Survey>> =
        MutableStateFlow(value = PagingData.empty())
    val surveyState: StateFlow<PagingData<Survey>> = _surveyState

    fun fetchSurveys() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 5,
                    prefetchDistance = 5,
                ),
                pagingSourceFactory = {
                    SurveyPagingSource()
                }
            ).flow.cachedIn(viewModelScope).collectLatest {
                _surveyState.value = it
            }
        }
    }
}