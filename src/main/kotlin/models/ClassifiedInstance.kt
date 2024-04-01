package org.example.models


data class ClassifiedInstance<T, R>(val attributes: Attributes<T>, val result: R)