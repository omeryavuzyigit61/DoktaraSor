package com.dombikpanda.doktarasor.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dombikpanda.doktarasor.adapter.ListAdapter
import com.dombikpanda.doktarasor.databinding.FragmentDoctorListBinding
import com.dombikpanda.doktarasor.hide
import com.dombikpanda.doktarasor.data.model.Questionio
import com.dombikpanda.doktarasor.ui.view.activity.DoctorAnswerActivity
import com.dombikpanda.doktarasor.ui.viewmodel.ListViewModel
import com.google.firebase.firestore.Query

class DoctorListFragment : Fragment(), ListAdapter.onItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentDoctorListBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var listViewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var question: Questionio
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        userId = listViewModel.firebaseAuth.uid!!
        listAdapter = ListAdapter(this)
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter
            }
            observeData()
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        listViewModel.apply {
            readUser(userId).observe(viewLifecycleOwner, {
                setListData(it).apply {
                    val policlinicStr = policlinic
                    fetchQuestinData(
                        "questions",
                        requireContext(),
                        "cevapdurum",
                        false,
                        "policinic",
                        policlinicStr,
                        Query.Direction.DESCENDING
                    )
                        .observe(viewLifecycleOwner, {
                            binding.shimmerViewContainer.startShimmer()
                            binding.shimmerViewContainer.hideShimmer()
                            binding.shimmerViewContainer.hide()
                            listAdapter.setListData(it)
                            listAdapter.notifyDataSetChanged()
                        })
                }
            })
        }
    }

    override fun onItemClick(position: Int) {
        question = listAdapter.dataList[position] as Questionio
        val intent = Intent(requireContext(), DoctorAnswerActivity::class.java)
        intent.putExtra("questionId", question.questionid)
        intent.putExtra("userId", question.userid)
        startActivity(intent)
    }

}