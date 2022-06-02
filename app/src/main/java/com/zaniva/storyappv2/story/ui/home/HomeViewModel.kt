package com.zaniva.storyappv2.story.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.zaniva.storyappv2.connection.Connection
import com.zaniva.storyappv2.connection.SessionManager
import com.zaniva.storyappv2.story.Stories
import com.zaniva.storyappv2.story.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: SessionManager) : ViewModel() {

    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _list = MutableLiveData<List<Stories>>()
    val list: LiveData<List<Stories>> = _list

    fun setStories(token: String){
        Connection.apiIns
            .listStory("Bearer $token")
            .enqueue(object : Callback<StoryResponse>{
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    _load.value = true
                    if (response.isSuccessful){
                        if (response.body() != null){
                            _list.value = response.body()?.list
                            _state.postValue(response.body()?.state)
                            Log.i("request_success", response.body().toString())
                        } else {
                            _state.postValue(response.body()?.state)
                            Log.i("response_failed", response.body().toString())
                        }
                    }
                    _load.value = false
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _load.value = true
                    Log.d("Failed", t.message.toString())
                    _load.value = false
                }
            })
    }

    fun getToken(): LiveData<String>{
        val tkn = pref.getToken().asLiveData()
        return tkn
    }

    fun getName(): LiveData<String>{
        val name = pref.getName().asLiveData()
        return name
    }


    fun getStories(): LiveData<List<Stories>>{
        return list
    }
}