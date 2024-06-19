package dev.sohair.jsontask.data.remote

import dev.sohair.jsontask.data.PostDto
import dev.sohair.jsontask.data.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface PlaceholderApi {

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("posts")
    suspend fun getPost(): Response<List<PostDto>>
}