package com.example.trackify.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackify.R
import com.example.trackify.adapters.TransactionAdapter
import com.example.trackify.data.TransactionData
import com.example.trackify.databinding.ActivityExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ExpenseActivity : AppCompatActivity() {
    private lateinit var bind : ActivityExpenseBinding
    private lateinit var fStore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    // data for history to be put into recycler view
    private lateinit var history_list : ArrayList<TransactionData>

    // for total expense, income and balance
    private var total_expense : Int = 0
    private var total_income : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityExpenseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!! // used for null exception
        history_list = ArrayList<TransactionData>()

        // loading data in list to display in recycler view
        loadData()

        bind.logout.setOnClickListener {
            logoutUser()
        }

        bind.refresh.setOnClickListener {
            val intent = Intent(this,ExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }

        bind.recyclerViewTransactions.layoutManager = LinearLayoutManager(this)
        bind.recyclerViewTransactions.setHasFixedSize(true)

        bind.addTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logoutUser() {
        // create dialog box here
        val dialog_box = AlertDialog.Builder(this)
        dialog_box.setTitle("Are You Sure?")
        dialog_box.setMessage("Do you want to Log Out?")
        dialog_box.setIcon(R.drawable.log_custom)
        dialog_box.setPositiveButton("YES",DialogInterface.OnClickListener { dialogInterface, i ->
            val intent = Intent(this,IntroActivity::class.java)
            startActivity(intent)
            finish()
        })
        dialog_box.setNegativeButton("NO",DialogInterface.OnClickListener { dialogInterface, i ->

        })
        dialog_box.show()

    }

    // function to load data from fire store
    private fun loadData() {
        val id = UUID.randomUUID().toString()
        // take care of this
        history_list.clear()

        auth.uid?.let { fStore.collection("Transactions").document(it).collection("Details")
            .get().addOnSuccessListener {
                if(it.isEmpty == false){
                    for(data in it.documents){
                        val amount = data.getString("Amount")!!
                        val id = data.getString("Id")!!
                        val note = data.getString("Note")!!
                        val type = data.getString("Type")!!
                        val time = data.getString("Time")!!

                        // for total expense, income and balance
                        val amount_given = Integer.parseInt(data.getString("Amount")!!)
                        if(type == "Expense"){
                            total_expense = total_expense + amount_given
                        }
                        else{
                            total_income = total_income + amount_given
                        }
                        val expenses = total_expense.toString()
                        val income = total_income.toString()
                        val balance = (total_income - total_expense).toString()

                        bind.expenseDisplay.text = expenses
                        bind.incomeDisplay.text = income
                        bind.balanceDisplay.text = balance


                        val transaction = TransactionData(amount,id,note,type,time)
                        if(transaction != null){
                            history_list.add(transaction)
                        }
                    }
                    bind.recyclerViewTransactions.adapter = TransactionAdapter(this, history_list)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}