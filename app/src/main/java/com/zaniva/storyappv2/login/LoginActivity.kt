package com.zaniva.storyappv2.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.register.RegisterActivity
import com.zaniva.storyappv2.connection.SessionManager
import com.zaniva.storyappv2.databinding.ActivityLoginBinding
import com.zaniva.storyappv2.story.HomeActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sVM : LoginVm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionManager.get(dataStore)
        sVM = ViewModelProvider(this, LoginVMFactory(pref)).get(
            LoginVm::class.java
        )

        setMyButtonEnable()

        binding.apply {

            pbHome.visibility = View.GONE

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }
                override fun afterTextChanged(s: Editable) {
                }
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }
                override fun afterTextChanged(s: Editable) {
                }
            })

            btRegister.setOnClickListener{
                register()
            }

            btSignin.setOnClickListener {
                val em = binding.etEmail.text.toString()
                val pass = binding.etPassword.text.toString()
                login(em,pass)
            }

        }



    }

    private fun login(em: String, pass: String){
        showLoading(true)
        sVM.set(em, pass)
        sVM.load.observe(this){
            if (it == false){
                showLoading(it)
            }
        }
        sVM.state.observe(this){ state ->
            if(!state){
                var name = "Guest"
                sVM.result.observe(this){
                    sVM.save(it.token, it.name)
                    name = it.name
                }
                Toast.makeText(this, "Logged In! Welcome $name", Toast.LENGTH_SHORT).show()
                Intent(this@LoginActivity, HomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }



            } else {
                Toast.makeText(this, "Failed to Login!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun showLoading(load:Boolean){
            if (load){
                binding.pbHome.visibility = View.VISIBLE
            } else {
                binding.pbHome.visibility = View.GONE
            }
    }

    private fun setMyButtonEnable() {
        val em = binding.etEmail.text.toString()
        val pass = binding.etPassword.length()
        binding.btSignin.isEnabled = Patterns.EMAIL_ADDRESS.matcher(em).matches() && pass >= 6
    }

    private fun register(){
        binding.apply{
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}