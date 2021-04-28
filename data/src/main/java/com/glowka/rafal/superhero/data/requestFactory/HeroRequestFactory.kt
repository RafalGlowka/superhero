package com.glowka.rafal.superhero.data.requestFactory

import com.glowka.rafal.superhero.data.remote.RequestType
import com.glowka.rafal.superhero.data.remote.RestRequest
import com.glowka.rafal.superhero.domain.model.Hero

/**
 * Created by Rafal on 27.04.2021.
 */

class SearchByNameResponseDTO(
  val response: String,
  val results: List<Hero>?
)


interface HeroRequestFactory {
  fun createSearchByName(name: String): RestRequest<SearchByNameResponseDTO>

  fun createSearchById(id: String): RestRequest<Hero>
}

class HeroRequestFactoryImpl() : HeroRequestFactory {

  companion object {
    const val BASE_URL = "https://superheroapi.com/api.php/10216227941362245/"
  }

  override fun createSearchByName(name: String): RestRequest<SearchByNameResponseDTO> {
    return RestRequest(
      type = RequestType.GET,
      url = BASE_URL + "search/$name",
      resultClass = SearchByNameResponseDTO::class
    )
  }

  override fun createSearchById(id: String): RestRequest<Hero> {
    return RestRequest(
      type = RequestType.GET,
      url = BASE_URL + id,
      resultClass = Hero::class
    )
  }


}
