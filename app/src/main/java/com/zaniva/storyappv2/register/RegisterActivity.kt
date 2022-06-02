package com.zaniva.storyappv2.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.databinding.ActivityRegisterBinding
import com.zaniva.storyappv2.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sVM: RegisterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sVM = ViewModelProvider(this).get(RegisterVM::class.java)

        setMyButtonEnable()

        binding.apply {
            binding.pbHome.visibility = View.GONE

            setEtName()

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {
                }
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {
                }
            })

            btRegister.setOnClickListener {
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val pass = binding.etPassword.text.toString()

                register(name, email, pass)
            }
        }
    }

    private fun showLoading(load: Boolean) {
        if (load) {
            binding.pbHome.visibility = View.VISIBLE
        } else {
            binding.pbHome.visibility = View.GONE
        }
    }

    private fun setMyButtonEnable() {
        val em = binding.etEmail.text.toString()
        val pass = binding.etPassword.length()
        binding.btRegister.isEnabled = Patterns.EMAIL_ADDRESS.matcher(em)
            .matches() && pass >= 6 && binding.etName.text!!.isNotEmpty()
    }

    private fun setEtName() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun register(name: String, email: String, pass: String) {
        showLoading(true)
        sVM.set(name, email, pass)
        sVM.load.observe(this) {
            if (it == false) {
                showLoading(it)
            }
        }
        sVM.state.observe(this) { state ->
            if (!state) {
                Toast.makeText(
                    this,
                    "Success! Please Log In using the registered account",
                    Toast.LENGTH_LONG
                ).show()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                sVM.result.observe(this) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}