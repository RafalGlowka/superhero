package com.glowka.rafal.superhero.data

import android.content.SharedPreferences
import com.glowka.rafal.superhero.data.remote.JSONSerializer
import com.glowka.rafal.superhero.data.repository.FavouritesRepositoryImpl
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rafal on 27.04.2021.
 */
class FavouritesRepositoryTest {

  @MockK
  private lateinit var sharedPreferences: SharedPreferences

  @MockK
  private lateinit var jsonSerializer: JSONSerializer

  lateinit var repository: FavouritesRepository

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    repository = FavouritesRepositoryImpl(
      sharedPreferences = sharedPreferences,
      jsonSerializer = jsonSerializer,
    )
  }

  @After
  fun finish() {
    confirmVerified(
      sharedPreferences,
      jsonSerializer
    )
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun readingDataFromSharedPreferences() {
    // Given
    val JSONDATA = "[123, 124, 125]"

    every {
      sharedPreferences.getString(
        FavouritesRepositoryImpl.FIELD_FAVOURITES,
        null
      )
    } returns JSONDATA
    every {
      jsonSerializer.fromJSON<FavouritesRepositoryImpl.FavouritesFieldType>(
        JSONDATA,
        FavouritesRepositoryImpl.FavouritesFieldType::class.java
      )
    } returns FavouritesRepositoryImpl.FavouritesFieldType()

    // When
    val testObserver = repository.loadFavorites().test()

    // Then
    testObserver.assertComplete()
    verify { sharedPreferences.getString(FavouritesRepositoryImpl.FIELD_FAVOURITES, null) }
    verify {
      jsonSerializer.fromJSON<FavouritesRepositoryImpl.FavouritesFieldType>(
        JSONDATA,
        FavouritesRepositoryImpl.FavouritesFieldType::class.java
      )
    }
  }

  @Test
  fun savingDataToSharedPreferences() {
    // Given
    val favourites = listOf("123", "124", "125")
    val jsonFavourites = "[]"
    val editor: SharedPreferences.Editor = mockk()
    every { jsonSerializer.toJSON(favourites) } returns jsonFavourites
    every { sharedPreferences.edit() } returns editor
    every {
      editor.putString(
        FavouritesRepositoryImpl.FIELD_FAVOURITES,
        jsonFavourites
      )
    } returns editor
    every { editor.commit() } returns true

    // When
    val testObserver = repository.saveFavourites(favourities = favourites).test()

    //Then
    testObserver.assertComplete()
    verify { jsonSerializer.toJSON(favourites) }
    verify { sharedPreferences.edit() }
  }

}