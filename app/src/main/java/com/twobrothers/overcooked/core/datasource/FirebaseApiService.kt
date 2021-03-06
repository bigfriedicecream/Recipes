package com.twobrothers.overcooked.core.datasource

import com.twobrothers.overcooked.core.responses.GetFirebaseRecipeResponse
import com.twobrothers.overcooked.core.responses.FirebaseRecipeList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines all the endpoints provided by the Firebase API. This interface is then implemented by Retrofit.
 */
interface FirebaseApiService {

    @GET("/getRecipes")
    suspend fun getRecipes(
        @Query("v") v: Double,
        @Query("env") env: Int
    ): Response<FirebaseRecipeList>

    @GET("/getRecipe")
    suspend fun getRecipe(
        @Query("v") v: Double,
        @Query("env") env: Int,
        @Query("id") id: String
    ): Response<GetFirebaseRecipeResponse>

}