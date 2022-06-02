package com.zaniva.storyappv2.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.R
import com.zaniva.storyappv2.connection.SessionManager
import com.zaniva.storyappv2.login.LoginActivity
import com.zaniva.storyappv2.story.HomeActivity
import com.zaniva.storyappv2.story.ui.home.HomeFragment
import com.zaniva.storyappv2.story.ui.home.HomeVMFactory
import com.zaniva.storyappv2.story.ui.home.HomeViewModel

class SplashScreen : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ssactivity)

        val pref = SessionManager.get(dataStore)
        val sVM = ViewModelProvider(this, HomeVMFactory(pref)).get(
            HomeViewModel::class.java
        )


        val delay = getString(R.string.delay).toLong()
        var token = "empty"
        var name = "Guest"

        supportActionBar?.hide()



        Handler().postDelayed({
            sVM.getToken().observe(this) {
                sVM.getName().observe(this){
                    if (it != null){
                        name = it
                    }
                }
                if (it != null){
                    token = it
                }
                if (token != "empty"){
                    Toast.makeText(this, "Welcome back, $name", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }, delay)
    }
}