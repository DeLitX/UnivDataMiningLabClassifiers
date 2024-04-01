package org.example.classifiers

import org.example.models.Attributes
import org.example.models.ClassifiedInstance

interface Classifier<T, R> {
    fun insertClassifiedData(classifiedData: List<ClassifiedInstance<T, R>>)
    fun classify(attributes: Attributes<T>): R
}
