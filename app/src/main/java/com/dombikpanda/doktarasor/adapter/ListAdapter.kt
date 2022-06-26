package com.dombikpanda.doktarasor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.SimpleDateFormat
import com.dombikpanda.doktarasor.io.Questionio
import com.dombikpanda.doktarasor.repository.CrudRepository

class ListAdapter(
    val listener: onItemClickListener,
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    val crudRepository = CrudRepository()
    private var questionMap: HashMap<String, Any> = HashMap()

    var dataList = mutableListOf<Any>()
    private lateinit var question: Questionio

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setListData(data: MutableList<Any>) {
        dataList = data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(i: Int, context: Context) {
        question = dataList[i] as Questionio
        dataList.removeAt(i)
        questionMap["messageDurum"] = false
        crudRepository.allUpdate(questionMap,"questions",context,question.questionid)
        notifyDataSetChanged()
    }

    /*@SuppressLint("NotifyDataSetChanged")
    fun addItem(i: Int, questionio: Questionio) {
        dataList.add(i, questionio)
        notifyDataSetChanged()
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        question = dataList[position] as Questionio
        holder.bindView(question)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            return 0
        }
    }

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(questionio: Questionio) {
            itemView.findViewById<TextView>(R.id.txt_policlinic).text = questionio.policinic
            itemView.findViewById<TextView>(R.id.txt_title).text = questionio.title
            itemView.findViewById<TextView>(R.id.txt_description).text = questionio.descripiton
            itemView.findViewById<TextView>(R.id.txt_date).text = SimpleDateFormat(questionio.date, "dd MMMM yyyy")
            itemView.findViewById<TextView>(R.id.txt_time).text = SimpleDateFormat(questionio.date, "HH:mm")
            val txtCevapDurum = itemView.findViewById<TextView>(R.id.txt_CevapDurum)
            if (questionio.cevapdurum == true) {
                txtCevapDurum.setText(R.string.answered)
                txtCevapDurum.setTextColor(Color.GREEN)
            } else {
                txtCevapDurum.setText(R.string.not_answered)
                txtCevapDurum.setTextColor(Color.RED)
            }
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}