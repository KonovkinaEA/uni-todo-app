package com.example.unitodoapp.data.api

import com.example.unitodoapp.data.api.model.ItemResponse
import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.data.api.model.TodoListContainer
import com.example.unitodoapp.data.api.model.TodoListResponse
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

    @GET("list")
    suspend fun getAllTodoData(): Response<TodoListResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header("revision") revision: String, @Body element: TodoItemServer
    ): Response<ItemResponse>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("revision") revision: String, @Path("id") id: String, @Body element: TodoItemServer
    ): Response<ItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("revision") revision: String, @Path("id") id: String
    ): Response<ItemResponse>

    @PATCH("db")
    suspend fun patchList(
        @Header("revision") revision: String, @Body list: TodoListContainer
    ): Response<TodoListResponse>
}