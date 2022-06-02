package com.zaniva.storyappv2.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaniva.storyappv2.connection.Connection
import com.zaniva.storyappv2.data.LoginResponse
import com.zaniva.storyappv2.data.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterVM : ViewModel() {
    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun set(name: String, email: String, password: String){
        Connection.apiIns
            .regisUser(name, email, password)
            .enqueue(object : Callback<RegisterResponse>{

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful){
                        _load.value = true
                        _result.value = response.body()?.result
                        _state.value = response.body()?.state

                        Log.i("response_success", response.body().toString())
                    } else {
                        _load.value = true
                        Log.i("response_failed", response.body().toString())
                    }
                    _load.value = false
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _load.value = true
                    Log.d("Failed", t.message.toString())
                    _load.value = false
                }
            })
    }

}