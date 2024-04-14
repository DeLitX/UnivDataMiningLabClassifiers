package org.example.models


data class Attributes<out T>(val values: List<T>) {
    constructor(vararg values: T) : this(values.toList())
}