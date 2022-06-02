package com.zaniva.storyappv2.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaniva.storyappv2.connection.Connection
import com.zaniva.storyappv2.story.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadVM : ViewModel() {
    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun upload(token: String, desc: String, img: MultipartBody.Part){
        Connection.apiIns
            .uploadStory("Bearer $token", desc, img)
            .enqueue(object : Callback<FileUploadResponse>{
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    _load.value = true
                    if (response.isSuccessful){
                        _result.value = response.body()?.message
                        _state.value = response.body()?.error

                        Log.i("response_success", response.body().toString())
                    } else {
                        _state.value = true
                        Log.i("response_failed", response.body().toString())
                    }
                    _load.value = false
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    _load.value = true
                    Log.d("Fail", t.message.toString())
                    _load.value = false
                }
            })
    }
}