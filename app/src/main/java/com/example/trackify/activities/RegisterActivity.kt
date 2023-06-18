package com.example.trackify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trackify.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var bind : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        auth = Firebase.auth

        bind.registerButton.setOnClickListener {
            val email = bind.emailRegister.text.toString()
            val password = bind.passwordRegister.text.toString()

            if(validateData()){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    // success
                    if(it.isSuccessful){
                        Toast.makeText(this,"User Registered Successfully!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
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
        if(bind.emailRegister.text.toString().isEmpty() || bind.passwordRegister.text.toString().isEmpty()){
            Toast.makeText(this,"Pls fill the details!",Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }
}