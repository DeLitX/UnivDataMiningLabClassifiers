package org.example.classifiers

import org.example.models.Attributes
import org.example.models.ClassifiedInstance
import kotlin.math.pow
import kotlin.math.sqrt

class KNearestNeighborsClassifier<T : Number, R>(private val k: Int) : Classifier<T, R> {
    private var classifiedData: List<ClassifiedInstance<T, R>> = emptyList()

    override fun insertClassifiedData(classifiedData: List<ClassifiedInstance<T, R>>) {
        this.classifiedData = classifiedData
    }

    override fun classify(attributes: Attributes<T>): R {
        if (classifiedData.isEmpty()) throw Exception("Classified data is empty")

        val distances = classifiedData.map { instance ->
            val distance = calculateDistance(instance.attributes, attributes)
            instance to distance
        }.sortedBy { it.second }

        val nearestNeighbors = distances.take(k)
        return majorityVote(nearestNeighbors.map { it.first.result })
    }

    private fun calculateDistance(a: Attributes<T>, b: Attributes<T>): Double {
        return sqrt(a.values.zip(b.values).sumOf { (x, y) ->
            (x.toDouble() - y.toDouble()).pow(2)
        })
    }

    private fun majorityVote(nearestResults: List<R>): R {
        return nearestResults.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            ?: throw Exception("Cannot determine majority vote")
    }
}
