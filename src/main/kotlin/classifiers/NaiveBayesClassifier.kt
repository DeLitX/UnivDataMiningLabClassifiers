package org.example.classifiers

import org.example.models.Attributes
import org.example.models.ClassifiedInstance
import kotlin.math.ln

class NaiveBayesClassifier<T, R> : Classifier<T, R> {

    private var classProbabilities: Map<R, Double> = emptyMap()
    private var conditionalProbabilities: Map<R, Map<Int, Map<T, Double>>> = emptyMap()

    override fun insertClassifiedData(classifiedData: List<ClassifiedInstance<T, R>>) {
        val classFrequency = mutableMapOf<R, Int>()
        val attributeFrequency = mutableMapOf<R, MutableList<MutableMap<T, Int>>>()

        classifiedData.forEach { instance ->
            classFrequency[instance.result] = classFrequency.getOrDefault(instance.result, 0) + 1
            instance.attributes.values.forEachIndexed { index, attribute ->
                if (attributeFrequency[instance.result] == null) {
                    attributeFrequency[instance.result] = MutableList(instance.attributes.values.size) { mutableMapOf() }
                }
                val frequencyMap = attributeFrequency[instance.result]!![index]
                frequencyMap[attribute] = frequencyMap.getOrDefault(attribute, 0) + 1
            }
        }

        classProbabilities = classFrequency.mapValues { (_, count) -> count.toDouble() / classifiedData.size }

        conditionalProbabilities = attributeFrequency.mapValues { (result, attributeList) ->
            attributeList.mapIndexed { index, attributeMap ->
                index to attributeMap.mapValues { (_, count) ->
                    (count + 1).toDouble() / (classFrequency[result]!! + attributeMap.size) // Applying Laplace smoothing
                }.toMap()
            }.toMap()
        }
    }

    override fun classify(attributes: Attributes<T>): R {
        val logProbabilities = classProbabilities.mapValues { (result, classProbability) ->
            val logClassProbability = ln(classProbability)
            val logConditionalProbability = attributes.values.mapIndexed { index, attribute ->
                ln(
                    conditionalProbabilities[result]?.get(index)?.get(attribute)
                        ?: (1.0 / (classProbabilities[result]!! + 1))
                ) // Applying Laplace smoothing for unseen attributes
            }.sum()
            logClassProbability + logConditionalProbability
        }

        return logProbabilities.maxByOrNull { it.value }!!.key
    }
}