package com.example.pagoflex.data.repository

interface RepositorioBase<T> {
    fun obtenerTodos(): List<T>
}
