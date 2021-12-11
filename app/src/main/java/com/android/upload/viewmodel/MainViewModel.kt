package com.android.upload.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.upload.domain.Result
import com.android.upload.domain.entity.Response
import com.android.upload.domain.usecase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {
    private val _feedBack = MutableLiveData<Response?>()
    val feedBack = _feedBack

    suspend fun uploadImage(body: MultipartBody.Part) {
        viewModelScope.launch {
            when (val result = uploadImageUseCase.invoke(body)) {
                is Result.Success -> {
                    Log.d(TAG, "${result.data}")
                    _feedBack.postValue(result.data)
                }
                is Result.Error -> {
                    Log.d(TAG, "${result.exception.message}")
                }
            }
        }
    }

    companion object {
        const val TAG = "MainVM"
    }
}