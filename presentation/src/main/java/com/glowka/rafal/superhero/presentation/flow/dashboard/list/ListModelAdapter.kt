package com.glowka.rafal.superhero.presentation.flow.dashboard.list

import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.databinding.ListItemHeroBinding
import com.glowka.rafal.superhero.presentation.utils.BaseModelItem
import com.glowka.rafal.superhero.presentation.utils.exhaustive
import com.mikepenz.fastadapter.adapters.GenericModelAdapter

/**
 * Created by Rafal on 17.04.2021.
 */
class HeroModelItem(model: ListItemModel.HeroCard) :
  BaseModelItem<ListItemModel.HeroCard, ListItemHeroBinding>(
    model = model,
    layoutRes = R.layout.list_item_hero,
    type = R.id.hero_list_hero
  )

class ListModelAdapter : GenericModelAdapter<ListItemModel>({ model ->
  when (model) {
    is ListItemModel.HeroCard -> HeroModelItem(model = model)
  }.exhaustive
}
)