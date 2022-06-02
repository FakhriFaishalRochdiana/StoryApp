package com.zaniva.storyappv2.login

import android.util.Log
import androidx.lifecycle.*
import com.zaniva.storyappv2.connection.Connection
import com.zaniva.storyappv2.connection.SessionManager
import com.zaniva.storyappv2.data.LoginResponse
import com.zaniva.storyappv2.data.Session
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginVm(private val pref: SessionManager) : ViewModel() {
    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    val _result = MutableLiveData<Session>()
    val result: LiveData<Session> = _result

    fun set(email: String, password: String) {

        Connection.apiIns
            .loginUser(email, password)
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _load.value = true
                    if (response.isSuccessful){
                        _result.value = response.body()?.result
                        _state.value = response.body()?.state

                        Log.i("response_success", response.body().toString())
                    } else {
                        _state.value = true
                        Log.i("response_failed", response.body().toString())
                    }
                    _load.value = false
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _load.value = true
                    Log.d("Fail", t.message.toString())
                    _load.value = false
                }
            })
    }




    fun save (token: String, name: String){
        viewModelScope.launch {
            pref.save(token, name)
        }
    }



}