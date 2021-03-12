package com.francisco.geovane.marcello.felipe.projetofinalandroid.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.MainActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_sign_up
import kotlinx.android.synthetic.main.activity_login.tv_password
import kotlinx.android.synthetic.main.activity_login.tv_username
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var bundle: Bundle = Bundle()
    private lateinit var analytics: FirebaseAnalytics
    private var TAG: String = "FIREBASE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = FirebaseAnalytics.getInstance(this)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        var appId: String = BuildConfig.APP_ID
        var pageId: String = this.localClassName

        btn_sign_up.setOnClickListener {

            bundle.clear()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "$appId:$pageId:btnSingUpClick")
            analytics?.logEvent("e_Click", bundle)

            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_log_in.setOnClickListener {

            bundle.clear()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "$appId:$pageId:btnLoginClick")
            analytics?.logEvent("e_Click", bundle)

            signInUser()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun signInUser() {
        val email: String = tv_username.text.toString()
        val password: String = tv_password.text.toString()
        hideKeyboard(currentFocus ?: View(this))

        if (email.isEmpty()) {
            tv_username.error = "Por favor insira um e-mail"
            tv_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tv_username.error = "Por favor insira um e-mail válido"
            tv_username.requestFocus()
            return
        }

        if (password.isEmpty()) {
            tv_password.error = "Por favor insira uma senha"
            tv_password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, task.exception)
                    Toast.makeText(baseContext, "Falha na autenticação",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
