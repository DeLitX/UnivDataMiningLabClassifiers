package org.example.models


data class Attributes<T>(val values: List<T>) {
    constructor(vararg values: T) : this(values.toList())
}