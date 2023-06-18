package com.example.trackify.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.trackify.databinding.ActivityTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : AppCompatActivity() {
    private lateinit var bind : ActivityTransactionBinding
    private var type = ""
    private lateinit var fStore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityTransactionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!! // used for null exception

        bind.isExpense.setOnClickListener {
            bind.isExpense.isChecked = true
            bind.isIncome.isChecked = false
            type = "Expense"
        }

        bind.isIncome.setOnClickListener {
            bind.isExpense.isChecked = false
            bind.isIncome.isChecked = true
            type = "Income"
        }

        bind.addButton.setOnClickListener {
            val amount = bind.amountAdded.text.toString().trim()
            val note = bind.noteAdded.text.toString().trim()

            // date and time
            val format = SimpleDateFormat("dd MM yyyy HH:mm:ss",Locale.getDefault())
            val time = format.format(Date())

            if(validateData()) {
                val id = UUID.randomUUID().toString()

                val added_info = hashMapOf(
                    "Amount" to amount,
                    "Note" to note,
                    "Type" to type,
                    "Id" to id,
                    "Time" to time
                )

                auth.uid?.let { it1 ->
                    fStore.collection("Transactions").document(it1).collection("Details")
                        .document(id)
                        .set(added_info).addOnCompleteListener {
                            // success
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Added Successfully!", Toast.LENGTH_SHORT)
                                    .show()
                                bind.amountAdded.text.clear()
                                bind.noteAdded.text.clear()
                                bind.isIncome.isChecked = false
                                bind.isExpense.isChecked = false

                                val intent = Intent(this,ExpenseActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            // failure
                            else {
                                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun validateData() : Boolean {
        val amount = bind.amountAdded.text.toString().trim()

        if(amount.length<=0){
            Toast.makeText(this,"Pls Fill The Amount!",Toast.LENGTH_SHORT).show()
            return false;
        }
        if(bind.isExpense.isChecked == false && bind.isIncome.isChecked == false){
            Toast.makeText(this,"Pls Mark One CheckBox!",Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }
}