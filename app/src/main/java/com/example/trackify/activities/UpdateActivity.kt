package com.example.trackify.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.trackify.R
import com.example.trackify.databinding.ActivityUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {
    private lateinit var bind : ActivityUpdateBinding
    var newType = ""


    private lateinit var fStore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        // fetching data from the recycler view
        val amount = intent.getStringExtra("Amount")!!
        val note = intent.getStringExtra("Note")!!
        val type = intent.getStringExtra("Type")!!

        // different set text for edit text view
        bind.amountAdded.setText(amount)
        bind.noteAdded.setText(note)

        // for checkboxes
        if(type == "Income"){
            newType = "Income"
            bind.isIncome.isChecked = true
            bind.isExpense.isChecked = false
        }
        else{
            newType = "Expense"
            bind.isIncome.isChecked = false
            bind.isExpense.isChecked = true
        }

        bind.isIncome.setOnClickListener {
            newType = "Income"
            bind.isIncome.isChecked = true
            bind.isExpense.isChecked = false
        }

        bind.isExpense.setOnClickListener {
            newType = "Expense"
            bind.isIncome.isChecked = false
            bind.isExpense.isChecked = true
        }

        bind.updateButton.setOnClickListener {
            // update information
            update_info()
        }

        bind.deleteButton.setOnClickListener {
            // delete information
            delete_info()
        }
    }

    private fun delete_info() {
        // creation of dialog box here to verify once again
        val dialog_box = AlertDialog.Builder(this)
        dialog_box.setTitle("Are You Sure?")
        dialog_box.setMessage("Do you want to Delete?")
        dialog_box.setIcon(R.drawable.delete)
        dialog_box.setPositiveButton("YES", DialogInterface.OnClickListener { dialogInterface, i ->
            val id = intent.getStringExtra("Id")!!

            auth.uid?.let { fStore.collection("Transactions").document(it).collection("Details")
                .document(id).delete().addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
                        bind.noteAdded.text.clear()
                        bind.isIncome.isChecked = false
                        bind.isExpense.isChecked = false
                        bind.amountAdded.text.clear()

                        val intent = Intent(this,ExpenseActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        dialog_box.setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, i ->
            Toast.makeText(this, "Data is still saved!", Toast.LENGTH_SHORT).show()
        })
        dialog_box.show()

    }

    private fun update_info() {
        val id = intent.getStringExtra("Id")!!

        if(validateData()){

            val dialog_box = AlertDialog.Builder(this)
            dialog_box.setTitle("Are You Sure?")
            dialog_box.setMessage("Do you want to Update?")
            dialog_box.setIcon(R.drawable.update)
            dialog_box.setPositiveButton("YES",DialogInterface.OnClickListener { dialogInterface, i ->
                val amount = bind.amountAdded.text.toString()
                val note = bind.noteAdded.text.toString()

                // date and time
                val format = SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.getDefault())
                val time = format.format(Date())

                auth.uid?.let { fStore.collection("Transactions").document(it).collection("Details")
                    .document(id).update("Amount",amount,"Note",note,"Type",newType,"Time",time)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Updated Successfully!", Toast.LENGTH_SHORT).show()
                            bind.noteAdded.text.clear()
                            bind.isIncome.isChecked = false
                            bind.isExpense.isChecked = false
                            bind.amountAdded.text.clear()

                            val intent = Intent(this,ExpenseActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            dialog_box.setNegativeButton("NO",DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(this, "Data is still saved!", Toast.LENGTH_SHORT).show()
            })
            dialog_box.show()

        }
    }

    private fun validateData(): Boolean {
        if(bind.amountAdded.length()<=0){
            Toast.makeText(this, "Pls Fill the Amount!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(bind.isExpense.isChecked == false && bind.isIncome.isChecked == false){
            Toast.makeText(this, "Pls Mark one CheckBox!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}