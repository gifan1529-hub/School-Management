package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.DiscussionData
import com.example.schoolmanagement.Domain.UseCase.GetDiscussionUseCase
import com.example.schoolmanagement.Domain.UseCase.SendDiscussionUseCase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscussionViewModel (
    private val getDiscussionUC: GetDiscussionUseCase,
    private val sendDiscussionUC: SendDiscussionUseCase,
    private val prefs: PrefsManager,
): ViewModel() {
    private val _discussions = MutableStateFlow<List<DiscussionData>>(emptyList())
    val discussions = _discussions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _myId = MutableStateFlow(0)
    val myId = _myId.asStateFlow()


    init {
        viewModelScope.launch {
            _myId.value = prefs.getUserData()?.id ?: 0 // Buat bedain bubble chat kiri/kanan
        }
    }

    fun observeChat(homeworkId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getDiscussionUC(homeworkId).collect { data ->
                _discussions.value = data
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(homeworkId: Int, message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            sendDiscussionUC(homeworkId, message)
        }
    }

//    fun loadDiscussions(homeworkId: Int,  isSilent: Boolean = false) {
//        viewModelScope.launch {
//            if (!isSilent) _isLoading.value = true // Loading cuma muncul pas isSilent false
//
//            getDiscussionUC(homeworkId).onSuccess { newData ->
//                if (_discussions.value != newData) {
//                    _discussions.value = newData
//                }
//            }
//            _isLoading.value = false
//        }
//    }
//
//    fun sendMessage(homeworkId: Int, message: String) {
//        if (message.isBlank()) return
//        viewModelScope.launch {
//            val result = sendDiscussionUC(homeworkId, message)
//            result.onSuccess {
//                loadDiscussions(homeworkId, isSilent = true)
//            }
//        }
//    }
}