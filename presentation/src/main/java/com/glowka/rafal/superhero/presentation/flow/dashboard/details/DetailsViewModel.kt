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
    header.label.text.postValue(param.hero.name)
    header.closeIcon.action.postValue(::closeScreen)
    val favIconResId = if (param.isFavourite) {
      R.drawable.ic_favorite_on
    } else {
      R.drawable.ic_favorite_off
    }
    header.favIcon.image.postValue(ImageModel(fallbackImageResId = favIconResId))
    header.favIcon.action.postValue(::onFavouriteClick)
    fullName.text.postValue(param.hero.biography?.`full-name` ?: String.EMPTY)
    image.image.postValue(
      ImageModel(
        url = param.hero.image.url,
        fallbackImageResId = R.drawable.hero_fallback
      )
    )
    stats.postValue(
      Stats(
        inteligence = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.intelligence),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.inteligence?.safeToInt() ?: 0
          )
        ),
        strength = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.strength),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.strength?.safeToInt() ?: 0
          )
        ),
        speed = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.speed),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.speed?.safeToInt() ?: 0
          )
        ),
        durability = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.durability),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.durability?.safeToInt() ?: 0
          )
        ),
        power = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.power),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.power?.safeToInt() ?: 0
          )
        ),
        combat = ProgressFieldBindingModel(
          initialLabel = stringResolver(R.string.combat),
          initialProgress = ProgressPosition(
            position = param.hero.powerstats?.combat?.safeToInt() ?: 0
          )
        ),
      )
    )
    alterEgo.value.text.postValue(param.hero.biography?.`alter-egos` ?: String.EMPTY)
  }

  private fun String.safeToInt(): Int {
    return try {
      toInt()
    } catch (nfe: NumberFormatException) {
      0
    }
  }

  private fun onFavouriteClick() {
    param = Param(hero = param.hero, isFavourite = !param.isFavourite)
    val favIconResId =
      if (param.isFavourite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
    header.favIcon.image.postValue(ImageModel(fallbackImageResId = favIconResId))

    changeIsHeroFavouriteUseCase(
      param = ChangeIsHeroFavouriteUseCase.Param(
        hero = param.hero,
        isFavourite = param.isFavourite
      )
    ).subscribeBy(
      onSuccess = {
      },
      onError = { error -> logE("saving changed", error) }
    ).disposedByHost()
  }

  private fun closeScreen() {
    sendEvent(event = Event.Back)
  }

}