package com.dombikpanda.doktarasor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.model.Doctor
import com.dombikpanda.doktarasor.repository.CrudRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber

class DoctorListAdapter(
    val listener: onItemClickListener,
) : RecyclerView.Adapter<DoctorListAdapter.ListViewHolder>() {

    var dataList = mutableListOf<Doctor>()
    private lateinit var doctor: Doctor
    private val crudRepository = CrudRepository()
    private var userMap: HashMap<String, Any> = HashMap()

    fun setListData(data: MutableList<Doctor>) {
        dataList = data
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_doctor_acceptance, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorListAdapter.ListViewHolder, position: Int) {
        doctor = dataList[position]
        holder.bindView(doctor)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            return 0
        }
    }

    fun accept(i: Int, context: Context) {
        doctor = dataList[i]
        userMap["kullaniciDurum"] = "1"
        crudRepository.allUpdate(userMap, "users", context, doctor.email)
    }
    fun failurte(i: Int, context: Context) {
        doctor = dataList[i]
        userMap["kullaniciDurum"] = "0"
        crudRepository.allUpdate(userMap, "users", context, doctor.email)
    }

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(doctor: Doctor) {
            val str: String
            val drawable: Int
            itemView.findViewById<TextView>(R.id.txt_Full_Name).text = doctor.fullname
            itemView.findViewById<TextView>(R.id.txt_Policlinic).text = doctor.policlinic
            itemView.findViewById<TextView>(R.id.txt_Phone).text = doctor.phone
            val currentTime = System.currentTimeMillis()
            val registeredDate = doctor.date
            val beetweenDate = currentTime - registeredDate
            val day = (beetweenDate / (1000 * 60 * 60 * 24))
            val hours = (beetweenDate / (1000 * 60 * 60))
            val mins = (beetweenDate / (1000 * 60))

            //1652693663926

            if (mins < 60) {
                str = mins.toString() + "d"
                drawable = R.drawable.ic_dot_green
            } else if (hours >= 1 && hours < 24) {
                str = hours.toString() + "s"
                drawable = R.drawable.ic_dot_orange
            } else {
                str = day.toString() + "g"
                drawable = R.drawable.ic_dot_red
            }
            itemView.findViewById<ImageView>(R.id.seen_ImageView).setImageResource(drawable)
            itemView.findViewById<TextView>(R.id.txt_Seen).text = str
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "users/ ${doctor.email}/profile.jpg"
            )
            sRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it)
                    .into(itemView.findViewById<CircleImageView>(R.id.profileImage))
            }.addOnFailureListener {
                Timber.i(it.message.toString())
            }
        }

        init {
            itemView.findViewById<ImageView>(R.id.succes_Image_View).setOnClickListener {
                listener.onItemClick(adapterPosition)
                accept(adapterPosition,itemView.context)
            }
            itemView.findViewById<ImageView>(R.id.failure_Image_View).setOnClickListener {
                listener.onItemClick(adapterPosition)
                failurte(adapterPosition,itemView.context)
            }
        }
    }
}