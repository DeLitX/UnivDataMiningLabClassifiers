package org.example.models


data class ClassifiedInstance<out T,out R>(val attributes: Attributes<T>, val result: R)