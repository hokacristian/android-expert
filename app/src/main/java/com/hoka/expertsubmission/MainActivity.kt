package com.hoka.expertsubmission

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoka.core.auth.UserRepository
import com.hoka.expertsubmission.authentication.AuthenticationViewModel
import com.hoka.expertsubmission.databinding.ActivityMainBinding
import com.hoka.expertsubmission.home.HomeActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModel()
    private val userRepository: UserRepository by inject()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userRepository.isUserLogin()) {
            moveToHome()
        }

        binding.btnLogin.isEnabled = false

        val emailStream = RxTextView.textChanges(binding.edEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.edPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream
        ) { emailInvalid, passwordInvalid ->
            !emailInvalid && !passwordInvalid
        }

        invalidFieldsStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        authenticationViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }
    }

    private fun login() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        try {
            authenticationViewModel.login(email, password)
        } catch (e: Exception) {
            Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        authenticationViewModel.loginResult.observe(this) { result ->
            if (result != null) {
                moveToHome()
            } else {
                Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.edEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.edPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }
}