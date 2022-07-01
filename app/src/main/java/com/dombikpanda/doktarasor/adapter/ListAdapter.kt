package com.dombikpanda.doktarasor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.SimpleDateFormat
import com.dombikpanda.doktarasor.data.model.Questionio
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.item_row.view.txt_description
import kotlinx.android.synthetic.main.item_row.view.txt_policlinic
import kotlinx.android.synthetic.main.item_row.view.txt_title

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
        crudRepository.allUpdate(questionMap, "questions", context, question.questionid)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        question = dataList[position] as Questionio
        holder.itemView.apply {
            txt_policlinic.text = question.policinic
            txt_title.text = question.title
            txt_description.text = question.descripiton
            txt_date.text =
                SimpleDateFormat(question.date, "dd MMMM yyyy")
            txt_time.text =
                SimpleDateFormat(question.date, "HH:mm")
            if (question.cevapdurum == true) {
                txt_CevapDurum.setText(R.string.answered)
                txt_CevapDurum.setTextColor(Color.GREEN)
            } else {
                txt_CevapDurum.setText(R.string.not_answered)
                txt_CevapDurum.setTextColor(Color.RED)
            }
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}