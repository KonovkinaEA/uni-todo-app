package com.example.unitodoapp.data.api

import com.example.unitodoapp.data.api.model.AuthResponse
import com.example.unitodoapp.data.api.model.EmailServer
import com.example.unitodoapp.data.api.model.ItemResponse
import com.example.unitodoapp.data.api.model.RecoveryResponse
import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.data.api.model.TodoListContainer
import com.example.unitodoapp.data.api.model.TodoListResponse
import com.example.unitodoapp.data.api.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("todos/{id}")
    suspend fun getAllTodoData(
        @Header("Authorization") accessToken: String, @Path("id") id: String
    ): Response<TodoListResponse>

    @POST("checkuser")
    suspend fun checkUserExist(
        @Body email: EmailServer
    ): Response<RecoveryResponse>

    @PUT("reset/{id}")
    suspend fun resetUser(
        @Path("id") id: String, @Body user: User
    )

    @POST("todos/{id}")
    suspend fun addTodoItem(
        @Header("revision") revision: String,
        @Header("Authorization") accessToken: String,
        @Path("id") id: String,
        @Body element: TodoItemServer
    ): Response<ItemResponse>

    @POST("register")
    suspend fun registerNewUser(
        @Body user: User
    ): Response<AuthResponse>

    @POST("login")
    suspend fun logInUser(
        @Body user: User
    ): Response<AuthResponse>

    @PUT("todos/{userId}/{id}")
    suspend fun updateTodoItem(
        @Header("revision") revision: String,
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: String,
        @Path("id") id: String,
        @Body element: TodoItemServer
    ): Response<ItemResponse>

    @DELETE("todos/{userId}/{id}")
    suspend fun deleteTodoItem(
        @Header("revision") revision: String,
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: String,
        @Path("id") id: String
    ): Response<ItemResponse>

    @PATCH("todos/{id}")
    suspend fun patchList(
        @Header("revision") revision: String,
        @Header("Authorization") accessToken: String,
        @Path("id") id: String,
        @Body list: TodoListContainer
    ): Response<TodoListResponse>
}