package org.example.classifiers

import org.example.models.Attributes
import org.example.models.ClassifiedInstance
import kotlin.math.log2

class DecisionTreeClassifier<T: Comparable<T>, R: Comparable<R>> : Classifier<T, R> {
    private var root: TreeNode<T, R>? = null

    override fun insertClassifiedData(classifiedData: List<ClassifiedInstance<T, R>>) {
        root = buildTree(classifiedData, classifiedData.first().attributes.values.indices.toList())
    }

    override fun classify(attributes: Attributes<T>): R {
        val rootNode = root ?: throw IllegalStateException("The decision tree has not been built yet.")
        return traverseTree(rootNode, attributes)
    }

    private fun buildTree(data: List<ClassifiedInstance<T, R>>, attributes: List<Int>): TreeNode<T, R>? {
        if (data.isEmpty()) return null
        if (data.map { it.result }.distinct().size == 1) return LeafNode(data.first().result)

        if (attributes.isEmpty()) {
            val mostCommonResult = data.groupingBy { it.result }.eachCount().maxByOrNull { it.value }!!.key
            return LeafNode(mostCommonResult)
        }

        val (bestAttribute, partitions, fallbackResult) = findBestAttribute(data, attributes)
        val remainingAttributes = attributes - bestAttribute

        val children = partitions.mapValues { (_, subset) -> buildTree(subset, remainingAttributes) }
        return TreeNode(bestAttribute, children, fallbackResult)
    }

    private fun findBestAttribute(data: List<ClassifiedInstance<T, R>>, attributes: List<Int>): Triple<Int, Map<T, List<ClassifiedInstance<T, R>>>, R> {
        var maxGain = Double.MIN_VALUE
        var bestAttribute = -1
        var bestPartitions = emptyMap<T, List<ClassifiedInstance<T, R>>>()
        var fallbackResult: R? = null

        for (attribute in attributes) {
            val partitions = data.groupBy { it.attributes.values[attribute] }
            val entropyBefore = entropy(data)
            val weightedEntropyAfter = partitions.values.sumOf { subset -> entropy(subset) * subset.size / data.size.toDouble() }
            val gain = entropyBefore - weightedEntropyAfter

            if (gain > maxGain) {
                maxGain = gain
                bestAttribute = attribute
                bestPartitions = partitions
                fallbackResult = partitions.values.flatten().groupingBy { it.result }.eachCount().maxByOrNull { it.value }?.key
            }
        }

        return Triple(bestAttribute, bestPartitions, fallbackResult!!)
    }

    private fun entropy(data: List<ClassifiedInstance<T, R>>): Double {
        val total = data.size.toDouble()
        return data.groupingBy { it.result }
            .eachCount()
            .values
            .sumOf { count ->
                val p = count / total
                -p * log2(p)
            }
    }

    private fun traverseTree(node: TreeNode<T, R>, attributes: Attributes<T>): R {
        return when (node) {
            is LeafNode -> node.result
            else -> {
                val attributeValue = attributes.values[node.attributeIndex]
                node.children[attributeValue]?.let { traverseTree(it, attributes) } ?: node.fallbackResult!!
            }
        }
    }
}

open class TreeNode<T, R>(var attributeIndex: Int, var children: Map<T, TreeNode<T, R>?>, var fallbackResult: R?){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TreeNode<*, *>

        if (attributeIndex != other.attributeIndex) return false
        if (children != other.children) return false
        if (fallbackResult != other.fallbackResult) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeIndex
        result = 31 * result + children.hashCode()
        result = 31 * result + (fallbackResult?.hashCode() ?: 0)
        return result
    }
}

data class LeafNode<T, R>(val result: R): TreeNode<T, R>(-1, emptyMap(), result)