package com.example.mymvvmnewsapp.util

/**
 * Lightweight wrapper that lets the UI react to Loading/Success/Error states without
 * duplicating boilerplate across fragments.
 */
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
){
    class Success<T> (data:T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T> (data,message)
    class Loading<T>: Resource<T>()
}