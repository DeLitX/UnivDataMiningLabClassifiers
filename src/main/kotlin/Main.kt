package org.example

import org.example.classifiers.*
import org.example.models.Attributes
import org.example.models.ClassifiedInstance

fun main() {
    val data1 = listOf(
        ClassifiedInstance(Attributes(0, 1, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 1), 1),
        ClassifiedInstance(Attributes(0, 1, 1), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
        ClassifiedInstance(Attributes(0, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 1, 2), 0),
        ClassifiedInstance(Attributes(1, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 0), 0),
        ClassifiedInstance(Attributes(0, 0, 0), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
    )

    val data2 = listOf(
        ClassifiedInstance(Attributes(0, 1, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 1), 0),
        ClassifiedInstance(Attributes(0, 1, 1), 1),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
        ClassifiedInstance(Attributes(0, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 1, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 2), 0),
        ClassifiedInstance(Attributes(1, 0, 0), 1),
        ClassifiedInstance(Attributes(0, 0, 0), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 0),
    )

    val data3 = listOf(
        ClassifiedInstance(Attributes(0, 1, 2), 0),
        ClassifiedInstance(Attributes(1, 0, 1), 0),
        ClassifiedInstance(Attributes(0, 1, 1), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 0),
        ClassifiedInstance(Attributes(0, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 1, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 0), 1),
        ClassifiedInstance(Attributes(0, 0, 0), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
    )

    val data4 = listOf(
        ClassifiedInstance(Attributes(0, 1, 2), 0),
        ClassifiedInstance(Attributes(1, 0, 1), 1),
        ClassifiedInstance(Attributes(0, 1, 1), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
        ClassifiedInstance(Attributes(0, 0, 2), 0),
        ClassifiedInstance(Attributes(1, 1, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 2), 1),
        ClassifiedInstance(Attributes(1, 0, 0), 1),
        ClassifiedInstance(Attributes(0, 0, 0), 0),
        ClassifiedInstance(Attributes(0, 0, 1), 1),
    )


    val dataToCheck = Attributes(1, 1, 1)

    val classifier: Classifier<Int, Int> = OneRuleClassifier()
//    val classifier: Classifier<Int, Int> = NaiveBayesClassifier()
//    val classifier: Classifier<Int, Int> = DecisionTreeClassifier()
//    val classifier: Classifier<Int, Int> = KNearestNeighborsClassifier(k = 5)

    classifier.insertClassifiedData(data1)
    println("Prediction for test instance1: ${classifier.classify(dataToCheck)}")
    classifier.insertClassifiedData(data2)
    println("Prediction for test instance2: ${classifier.classify(dataToCheck)}")
    classifier.insertClassifiedData(data3)
    println("Prediction for test instance3: ${classifier.classify(dataToCheck)}")
    classifier.insertClassifiedData(data4)
    println("Prediction for test instance4: ${classifier.classify(dataToCheck)}")
}
