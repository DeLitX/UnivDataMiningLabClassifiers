package org.example.classifiers

import org.example.models.Attributes
import org.example.models.ClassifiedInstance

class OneRuleClassifier<T : Int, R : Int> : Classifier<T, R> {

    private var ruleset: RuleSet<T, R>? = null
    private var attributeIndex: Int? = null

    override fun insertClassifiedData(classifiedData: List<ClassifiedInstance<T, R>>) {
        if (classifiedData.isEmpty()) return

        val frequencyTables = getFrequencyTables(classifiedData)
        val totalWeightsOfRules = List(classifiedData.first().attributes.values.size) { index ->
            classifiedData.sumOf { it.attributes.values[index] }
        }
        val rulesets = frequencyTables.map { getRuleset(it) }
        val rulesetsErrorRate = rulesets.mapIndexed { index, ruleset ->
            val totalWeightOfRule = totalWeightsOfRules[index]
            val errorRate = classifiedData.count { classifiedInstance ->
                val rule = ruleset.getRuleForInput(classifiedInstance.attributes.values[index])
                rule?.result != classifiedInstance.result
            }.toDouble() / totalWeightOfRule
            errorRate
        }
        val bestRulesetIndex = rulesetsErrorRate.indexOf(rulesetsErrorRate.minOrNull())

        if (bestRulesetIndex == -1) {
            throw Exception("No best ruleset found")
        }

        ruleset = rulesets[bestRulesetIndex]
        attributeIndex = bestRulesetIndex
    }

    override fun classify(attributes: Attributes<T>): R {
        val ruleset = ruleset ?: throw Exception("No ruleset found")
        val attributeIndex = attributeIndex ?: throw Exception("No attribute index found")
        val rule = ruleset.getRuleForInput(attributes.values[attributeIndex])
            ?: throw Exception("No rule found for input ${attributes.values[attributeIndex]}")
        return rule.result
    }

    private fun getFrequencyTables(classifiedData: List<ClassifiedInstance<T, R>>): List<Map<T, Map<R, Int>>> {
        val frequencyTables: MutableList<MutableMap<T, MutableMap<R, Int>>> =
            MutableList(classifiedData.first().attributes.values.size) { mutableMapOf() }
        for (classifiedInstance in classifiedData) {
            for ((attributeIndex, attribute) in classifiedInstance.attributes.values.withIndex()) {
                val frequencyTable = frequencyTables[attributeIndex]
                val result = classifiedInstance.result
                if (frequencyTable.containsKey(attribute)) {
                    val resultFrequency = frequencyTable[attribute]!!
                    resultFrequency[result] = resultFrequency.getOrDefault(result, 0) + 1
                } else {
                    frequencyTable[attribute] = mutableMapOf(result to 1)
                }
            }
        }
        return frequencyTables
    }

    private fun getRuleset(frequencyTable: Map<T, Map<R, Int>>): RuleSet<T, R> {
        val rules = frequencyTable.map { (attribute, resultFrequency) ->
            val result = resultFrequency.maxByOrNull { it.value }!!.key
            Rule(attribute, result)
        }
        return RuleSet(rules)
    }

    private data class Rule<T, R>(val input: T, val result: R)

    private data class RuleSet<T, R>(val rules: List<Rule<T, R>>) {
        fun getRuleForInput(input: T): Rule<T, R>? {
            return rules.find { it.input == input }
        }
    }
}