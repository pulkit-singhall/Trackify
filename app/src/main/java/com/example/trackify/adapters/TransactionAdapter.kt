package com.example.trackify.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.trackify.R
import com.example.trackify.activities.RegisterActivity
import com.example.trackify.activities.UpdateActivity
import com.example.trackify.data.TransactionData

class TransactionAdapter (val context : Activity, val history_list : ArrayList<TransactionData>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_item_recycler,parent,false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val current_item = history_list[position]

        holder.time.text = current_item.Time
        holder.expense.text = current_item.Amount
        holder.notes.text = current_item.Note
        if(current_item.Type.toString() == "Expense"){
            holder.type.setBackgroundResource(R.drawable.reg_bg)
            holder.expense.setTextColor(ContextCompat.getColor(context, R.color.g_red))
        }
        else{
            holder.type.setBackgroundResource(R.drawable.green_bg)
            holder.expense.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        // intent passing for update activity
        holder.itemView.setOnClickListener {
            val update_intent = Intent(context, UpdateActivity::class.java)
            update_intent.putExtra("Amount",current_item.Amount.toString())
            update_intent.putExtra("Note",current_item.Note.toString())
            update_intent.putExtra("Id",current_item.Id.toString())
            update_intent.putExtra("Type",current_item.Type.toString())
            context.startActivity(update_intent)
        }
    }

    override fun getItemCount() : Int {
        return history_list.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val notes = view.findViewById<TextView>(R.id.notes_section)
        val expense = view.findViewById<TextView>(R.id.expenses_section)
        val time = view.findViewById<TextView>(R.id.date_section)
        val type = view.findViewById<View>(R.id.type_view)
    }
}
