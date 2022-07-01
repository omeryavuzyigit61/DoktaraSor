package com.dombikpanda.doktarasor.ui.application

import android.app.Application
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.dombikpanda.doktarasor.ui.Factories.AnswerViewModelFactory
import com.dombikpanda.doktarasor.ui.Factories.HomeViewModelFactory
import com.dombikpanda.doktarasor.ui.viewmodel.HomeViewModel
import org.kodein.di.Kodein

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class HomeApplication : Application(),KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@HomeApplication))
        bind() from singleton { CrudRepository() }
        bind() from provider {
            HomeViewModelFactory(instance())
        }
        bind() from provider {
            AnswerViewModelFactory(instance())
        }
    }
}