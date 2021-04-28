package com.glowka.rafal.superhero.presentation.flow.dashboard.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.usecase.ChangeIsHeroFavouriteUseCase
import com.glowka.rafal.superhero.domain.utils.EMPTY
import com.glowka.rafal.superhero.domain.utils.StringResolver
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.architecture.BaseViewModel
import com.glowka.rafal.superhero.presentation.architecture.ScreenEvent
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.superhero.presentation.binding.ImageModel
import com.glowka.rafal.superhero.presentation.binding.fields.HeaderFieldBindingModel
import com.glowka.rafal.superhero.presentation.binding.fields.ProgressFieldBindingModel
import com.glowka.rafal.superhero.presentation.binding.fields.ProgressPosition
import com.glowka.rafal.superhero.presentation.binding.fields.TextFieldBindingModel
import com.glowka.rafal.superhero.presentation.binding.models.ImageBindingModel
import com.glowka.rafal.superhero.presentation.binding.models.TextBindingModel
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Event
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Param
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Rafal on 13.04.2021.
 */

interface DetailsViewModelToFlowInterface : ViewModelToFlowInterface<Param, Event> {
  data class Param(val hero: Hero, val isFavourite: Boolean)
  sealed class Event : ScreenEvent {
    object Back : Event()
  }
}

data class Stats(
  val inteligence: ProgressFieldBindingModel = ProgressFieldBindingModel(),
  val strength: ProgressFieldBindingModel = ProgressFieldBindingModel(),
  val speed: ProgressFieldBindingModel = ProgressFieldBindingModel(),
  val durability: ProgressFieldBindingModel = ProgressFieldBindingModel(),
  val power: ProgressFieldBindingModel = ProgressFieldBindingModel(),
  val combat: ProgressFieldBindingModel = ProgressFieldBindingModel(),
)

interface DetailsViewModelToViewInterface : ViewModelToViewInterface {
  val header: HeaderFieldBindingModel
  val image: ImageBindingModel
  val fullName: TextBindingModel
  val description: TextBindingModel
  val stats: LiveData<Stats>
  val alterEgo: TextFieldBindingModel
}

class DetailsViewModelImpl(
  private val stringResolver: StringResolver,
  private val changeIsHeroFavouriteUseCase: ChangeIsHeroFavouriteUseCase,
) : DetailsViewModelToViewInterface, DetailsViewModelToFlowInterface,
  BaseViewModel<Param, Event>(
    backPressedEvent = Event.Back
  ) {

  override val header = HeaderFieldBindingModel().apply { favIcon.visible.postValue(true) }
  override val image = ImageBindingModel()
  override val fullName = TextBindingModel()
  override val description = TextBindingModel()
  override val stats = MutableLiveData<Stats>()
  override val alterEgo =
    TextFieldBindingModel(initialLabel = stringResolver(R.string.label_alter_ego))

  lateinit var param: Param

  override fun init(param: Param) {
    this.param = param
    setHeader(param = param)
    setBasicData(hero = param.hero)
    updateStats(hero = param.hero)
  }

  private fun setHeader(param: Param) {
    header.label.text.postValue(param.hero.name)
    header.closeIcon.action.postValue(::closeScreen)
    val favIconResId = if (param.isFavourite) {
      R.drawable.ic_favorite_on
    } else {
      R.drawable.ic_favorite_off
    }
    header.favIcon.image.postValue(ImageModel(fallbackImageResId = favIconResId))
    header.favIcon.action.postValue(::onFavouriteClick)
  }

  private fun setBasicData(hero: Hero) {
    fullName.text.postValue(hero.biography?.`full-name` ?: String.EMPTY)
    image.image.postValue(
      ImageModel(
        url = hero.image.url,
        fallbackImageResId = R.drawable.hero_fallback
      )
    )
    alterEgo.value.text.postValue(hero.biography?.`alter-egos` ?: String.EMPTY)
  }

  private fun updateStats(hero: Hero) {
    stats.postValue(
      Stats(
        inteligence = progressField(
          labelResId = R.string.intelligence,
          value = hero.powerstats?.inteligence
        ),
        strength = progressField(
          labelResId = R.string.strength,
          value = hero.powerstats?.strength
        ),
        speed = progressField(
          labelResId = R.string.speed,
          value = hero.powerstats?.speed
        ),
        durability = progressField(
          labelResId = R.string.durability,
          value = hero.powerstats?.durability
        ),
        power = progressField(
          labelResId = R.string.power,
          value = hero.powerstats?.power
        ),
        combat = progressField(
          labelResId = R.string.combat,
          value = hero.powerstats?.combat
        ),
      )
    )
  }

  private fun progressField(labelResId: Int, value: String?) = ProgressFieldBindingModel(
    initialLabel = stringResolver(labelResId),
    initialProgress = ProgressPosition(
      position = value?.safeToInt() ?: 0
    )
  )

  private fun String.safeToInt(): Int {
    return try {
      toInt()
    } catch (nfe: NumberFormatException) {
      0
    }
  }

  private fun onFavouriteClick() {
    param = Param(hero = param.hero, isFavourite = !param.isFavourite)
    val favIconResId = if (param.isFavourite) {
      R.drawable.ic_favorite_on
    } else {
      R.drawable.ic_favorite_off
    }
    header.favIcon.image.postValue(ImageModel(fallbackImageResId = favIconResId))

    changeIsHeroFavouriteUseCase(
      param = ChangeIsHeroFavouriteUseCase.Param(
        hero = param.hero,
        isFavourite = param.isFavourite
      )
    ).subscribeBy(
      onSuccess = { _ ->
        // Nop
      },
      onError = { error -> logE("saving changed", error) }
    ).disposedByHost()
  }

  private fun closeScreen() {
    sendEvent(event = Event.Back)
  }

}