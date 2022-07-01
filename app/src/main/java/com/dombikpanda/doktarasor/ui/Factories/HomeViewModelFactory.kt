package com.dombikpanda.doktarasor.ui.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.dombikpanda.doktarasor.ui.viewmodel.HomeViewModel

class HomeViewModelFactory(
    private val repository: CrudRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}