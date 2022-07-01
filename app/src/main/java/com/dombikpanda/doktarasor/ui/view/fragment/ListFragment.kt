package com.dombikpanda.doktarasor.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.*
import com.dombikpanda.doktarasor.adapter.ListAdapter
import com.dombikpanda.doktarasor.databinding.FragmentListBinding
import com.dombikpanda.doktarasor.data.model.Questionio
import com.dombikpanda.doktarasor.di.SwipeGeusture
import com.dombikpanda.doktarasor.ui.view.activity.AnswerActivity
import com.dombikpanda.doktarasor.ui.viewmodel.ListViewModel
import com.google.firebase.firestore.Query

class ListFragment : Fragment(), ListAdapter.onItemClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var userId: String
    private lateinit var listAdapter: ListAdapter
    private lateinit var listViewModel: ListViewModel
    private lateinit var question: Questionio
    private var cevapMessageField: String = "messageDurum"
    private var cevapDurum: Boolean = true
    private var query: Query.Direction = Query.Direction.DESCENDING
    private var prefences: SharedPreferences? = null
    private var clicked = false
    private val fromAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_top_anim)
    }
    private val toAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_top_anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        userId = listViewModel.firebaseAuth.uid!!
        listAdapter = ListAdapter(this)
        prefences = activity?.getSharedPreferences("kontrol", Context.MODE_PRIVATE)
        binding.apply {
            fromTopToBottomBtn.setOnClickListener {
                onAddButtonClicked()
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter
            }
            observeData()
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (requireActivity().findViewById<RadioButton>(checkedId)) {
                    rbAnswered -> {
                        cevapMessageField = "cevapdurum"
                        cevapDurum = true
                        observeData()
                    }
                    rbNotAnswered -> {
                        cevapMessageField = "cevapdurum"
                        cevapDurum = false
                        observeData()
                    }
                    rbALL -> {
                        cevapMessageField = "messageDurum"
                        cevapDurum = true
                        observeData()
                    }
                }
            }
            radioGroup2.setOnCheckedChangeListener { _, checkId ->
                when (requireActivity().findViewById<RadioButton>(checkId)) {
                    rbAscending -> {
                        query = Query.Direction.ASCENDING
                        observeData()
                    }
                    rbDescending -> {
                        query = Query.Direction.DESCENDING
                        observeData()
                    }
                }
            }
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        val swipeGeusture = object : SwipeGeusture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        listAdapter.deleteItem(viewHolder.adapterPosition,requireContext())
                        val editor = prefences?.edit()
                        editor?.putString("todayQuestion", "0")
                        editor?.putString("date", "0")
                        editor?.apply()
                    }
                    ItemTouchHelper.RIGHT -> {

                    }
                }
            }
        }
        val touchHelper=ItemTouchHelper(swipeGeusture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        binding.shimmerViewContainer.startShimmer()
        listViewModel.fetchQuestinData(
            "questions",
            requireContext(),
            cevapMessageField,
            cevapDurum,
            "userid",
            userId,
            query
        )
            .observe(viewLifecycleOwner, {
                binding.shimmerViewContainer.startShimmer()
                binding.shimmerViewContainer.hideShimmer()
                binding.shimmerViewContainer.hide()
                listAdapter.setListData(it)
                listAdapter.notifyDataSetChanged()
            })
    }

    override fun onItemClick(position: Int) {
        question = listAdapter.dataList[position] as Questionio
        if (question.cevapdurum == true) {
            val intent = Intent(requireContext(), AnswerActivity::class.java)
            intent.putExtra("questionID", question.questionid)
            startActivity(intent)
        } else {
            showShortToast("Cevaplanmadı")
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.topMenu.show()
        } else {
            binding.topMenu.hide()
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.topMenu.startAnimation(fromAnimation)
        } else {
            binding.topMenu.startAnimation(toAnimation)
        }
    }
}
