package com.example.conversor2

class ConversionBrain {

    // Propriedades do estado atual
    var currentValue: Double = 0.0
    var fromUnit: String = "m"
    var toUnit: String = "km"
    var category: Category = Category.LENGTH

    // Categorias suportadas
    enum class Category {
        LENGTH, MASS, TEMPERATURE
    }

    // Função principal que chama a conversão adequada
    fun convert(value: Double, from: String, to: String, cat: Category): Double {
        this.currentValue = value
        this.fromUnit = from
        this.toUnit = to
        this.category = cat

        return when (cat) {
            Category.LENGTH -> convertLength(value, from, to)
            Category.MASS -> convertMass(value, from, to)
            Category.TEMPERATURE -> convertTemperature(value, from, to)
        }
    }

    // Conversão de comprimento
    private fun convertLength(value: Double, from: String, to: String): Double {
        val meters = when (from) {
            "m" -> value
            "km" -> value * 1000
            "cm" -> value / 100
            "mm" -> value / 1000
            "in" -> value * 0.0254
            "ft" -> value * 0.3048
            "yd" -> value * 0.9144
            "mi" -> value * 1609.34
            else -> value
        }

        return when (to) {
            "m" -> meters
            "km" -> meters / 1000
            "cm" -> meters * 100
            "mm" -> meters * 1000
            "in" -> meters / 0.0254
            "ft" -> meters / 0.3048
            "yd" -> meters / 0.9144
            "mi" -> meters / 1609.34
            else -> meters
        }
    }

    // Conversão de massa
    private fun convertMass(value: Double, from: String, to: String): Double {
        val kg = when (from) {
            "kg" -> value
            "g" -> value / 1000
            "mg" -> value / 1000000
            "lb" -> value * 0.453592
            "oz" -> value * 0.0283495
            "ton" -> value * 1000
            else -> value
        }

        return when (to) {
            "kg" -> kg
            "g" -> kg * 1000
            "mg" -> kg * 1000000
            "lb" -> kg / 0.453592
            "oz" -> kg / 0.0283495
            "ton" -> kg / 1000
            else -> kg
        }
    }

    // Conversão de temperatura
    private fun convertTemperature(value: Double, from: String, to: String): Double {
        return when {
            from == "°C" && to == "°F" -> (value * 9/5) + 32
            from == "°C" && to == "K" -> value + 273.15
            from == "°F" && to == "°C" -> (value - 32) * 5/9
            from == "°F" && to == "K" -> (value - 32) * 5/9 + 273.15
            from == "K" && to == "°C" -> value - 273.15
            from == "K" && to == "°F" -> (value - 273.15) * 9/5 + 32
            else -> value // mesma unidade
        }
    }
}
