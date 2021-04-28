package com.glowka.rafal.superhero.presentation.flow.dashboard.list

import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.usecase.LoadFavouritesUseCase
import com.glowka.rafal.superhero.domain.usecase.SearchByNameUseCase
import com.glowka.rafal.superhero.domain.utils.*
import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.architecture.BaseViewModel
import com.glowka.rafal.superhero.presentation.architecture.ScreenEvent
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.superhero.presentation.binding.models.EditTextBindingModel
import com.glowka.rafal.superhero.presentation.binding.models.TextBindingModel
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListViewModelToFlowInterface.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Rafal on 13.04.2021.
 */

interface ListViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    class ShowDetails(val hero: Hero, val isFavourite: Boolean) : Event()
    object Back : Event()
  }

  fun refreshFavourites()
}

interface ListViewModelToViewInterface : ViewModelToViewInterface {
  val searchField: EditTextBindingModel
  val items: MutableLiveData<List<ListItemModel>>
  val message: TextBindingModel
  fun onHeroPick(heroCard: ListItemModel.HeroCard)
}

class ListViewModelImpl(
  private val stringResolver: StringResolver,
  private val loadFavouritesUseCase: LoadFavouritesUseCase,
  searchByNameUseCase: SearchByNameUseCase,
) : ListViewModelToViewInterface, ListViewModelToFlowInterface, BaseViewModel<EmptyParam, Event>(
  backPressedEvent = Event.Back
) {
  override val searchField = EditTextBindingModel(
    initialHint = stringResolver(R.string.search_by_name_hint),
    initialText = stringResolver(R.string.search_by_name_initial_text)
  )
  override val items = MutableLiveData<List<ListItemModel>>()
  override val message = TextBindingModel(initialText = String.EMPTY)

  override fun onHeroPick(heroCard: ListItemModel.HeroCard) {
    sendEvent(event = Event.ShowDetails(hero = heroCard.hero, isFavourite = heroCard.isFavourite))
  }

  private var searchDisposable = CompositeDisposable()
  private var favourites: List<Hero>? = null

  init {
    refreshFavourites()

    searchField.text.observeForever { newValue ->
      searchDisposable.clear()
      searchDisposable = CompositeDisposable()
      if (newValue.isEmpty() || newValue.isBlank()) {
        favourites?.let { f -> updateList(heroes = f) } ?: logE("has no favourites")
      } else {
        searchByNameUseCase(param = newValue).subscribeBy(
          onSuccess = ::updateList,
          onError = { error ->
            logE("search", error)
          }
        ).addTo(searchDisposable)
      }
    }
  }

  private fun updateList(heroes: List<Hero>) {
    logD("updateList ${heroes.size}")
    if (heroes.isEmpty()) {
      val query = searchField.text.value
      val errorMessage = if (query?.isEmpty() != false) {
        stringResolver(R.string.missing_favourites)
      } else {
        stringResolver(R.string.missing_results, query)
      }
      message.text.postValue(errorMessage)
      message.visible.postValue(true)
      items.postValue(emptyList())
    } else {
      message.visible.postValue(false)
      items.postValue(heroes.map { hero ->
        ListItemModel.HeroCard(
          hero = hero,
          isFavourite = favourites?.contains(hero) ?: false
        )
      })
    }
  }

  override fun refreshFavourites() {
    loadFavouritesUseCase(EmptyParam.EMPTY)
      .subscribeBy(
        onSuccess = { result ->
          favourites = result
          if (searchField.text.value?.isBlank() == true || searchField.text.value?.isEmpty() == true) {
            updateList(result)
          } else {
            // Check if list elements need update.
            val updatedList = items.value?.toMutableList()?.map { item ->
              when (item) {
                is ListItemModel.HeroCard -> item.hero
              }
            } ?: emptyList()
            updateList(heroes = updatedList)
          }
        },
        onError = { error ->
          logE("getting favourites", error)
        }
      ).disposedByHost()

  }
}