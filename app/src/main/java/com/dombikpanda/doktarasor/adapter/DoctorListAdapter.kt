package com.dombikpanda.doktarasor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.data.model.Doctor
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_admin_doctor_acceptance.view.*
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

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_doctor_acceptance, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: DoctorListAdapter.ListViewHolder, position: Int) {
        doctor = dataList[position]
        val str: String
        val drawable: Int
        holder.itemView.apply{
            txt_Full_Name.text = doctor.fullname
            txt_Policlinic.text = doctor.policlinic
            txt_Phone.text = doctor.phone
            val currentTime = System.currentTimeMillis()
            val registeredDate = doctor.date
            val beetweenDate = currentTime - registeredDate
            val day = (beetweenDate / (1000 * 60 * 60 * 24))
            val hours = (beetweenDate / (1000 * 60 * 60))
            val mins = (beetweenDate / (1000 * 60))

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
            seen_ImageView.setImageResource(drawable)
            txt_Seen.text = str
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "users/ ${doctor.email}/profile.jpg"
            )
            sRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it)
                    .into(profileImage)
            }.addOnFailureListener {
                Timber.i(it.message.toString())
            }

            succes_Image_View.setOnClickListener {
                listener.onItemClick(position)
                accept(position, context)
            }
            failure_Image_View.setOnClickListener {
                listener.onItemClick(position)
                failurte(position, context)
            }
        }
    }
}
