package com.mobileapp.kurs.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("DELETE FROM cities WHERE id = :cityId")
    suspend fun deleteCityById(cityId: Int)

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM cities
            WHERE name = :name
            AND latitude = :latitude
            AND longitude = :longitude
        )
        """
    )
    suspend fun isCitySaved(
        name: String,
        latitude: Double,
        longitude: Double
    ): Boolean
}