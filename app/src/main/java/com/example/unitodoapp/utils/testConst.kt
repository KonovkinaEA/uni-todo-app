package com.example.unitodoapp.utils

import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem


val toDoList = listOf(
    TodoItem(
        text = "Купить что-то",
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                "но точно нужно чтобы показать как обрезается " +
                "эта часть текста не видна",
    ),
    TodoItem(
        text = "Купить что-то",
        importance = Importance.IMPORTANT
    ),
    TodoItem(
        text = "Купить что-то",
        isDone = true
    ),
    TodoItem(
        text = "Купить что-то",
        isDone = true
    ),
    TodoItem(
        text = "Купить что-то",
        isDone = true
    ),
    TodoItem(
        text = "Купить что-то",
        deadline = 1654665100000L
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                "но точно нужно чтобы показать как обрезается " +
                "эта часть текста не видна",
    ),
    TodoItem(
        text = "Купить что-то",
        importance = Importance.IMPORTANT
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                "но точно нужно чтобы показать как обрезается " +
                "эта часть текста не видна",
    ),
    TodoItem(
        text = "Купить что-то",
        importance = Importance.IMPORTANT
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                "но точно нужно чтобы показать как обрезается " +
                "эта часть текста не видна",
    ),
    TodoItem(
        text = "Купить что-то",
        importance = Importance.IMPORTANT
    ),
    TodoItem(
        text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                "но точно нужно чтобы показать как обрезается " +
                "эта часть текста не видна",
    ),
    TodoItem(
        text = "Купить что-то",
        importance = Importance.IMPORTANT
    )
)