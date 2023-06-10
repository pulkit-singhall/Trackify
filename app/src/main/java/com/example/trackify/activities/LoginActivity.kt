package com.example.trackify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trackify.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var bind : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        auth = FirebaseAuth.getInstance()

        bind.loginButton.setOnClickListener {
            val email = bind.emailLogin.text.toString()
            val password = bind.passwordLogin.text.toString()

            if(validateData()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    // success
                    if(it.isSuccessful){
                        Toast.makeText(this,"Login Successfully!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ExpenseActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    // failure
                    else{
                        Toast.makeText(this,"Failure!",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateData() : Boolean {
        if(bind.emailLogin.text.toString().isEmpty() || bind.passwordLogin.text.toString().isEmpty()){
            Toast.makeText(this,"Pls fill the details!", Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }
}