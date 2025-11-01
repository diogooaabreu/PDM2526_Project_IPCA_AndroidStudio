package ipca.example.calculator

class CalculatorBrain {

    enum class Operation(op: String) {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("×"),
        DIVIDE("÷"),
        EQUAL("="),
        SQRT("√"),
        PERCENTAGE("%"),
        CLEAR("C"),
        ALL_CLEAR("AC");

        companion object {
            fun parseOperation(op: String): Operation {
                return when (op) {
                    "+" -> ADD
                    "-" -> SUBTRACT
                    "×" -> MULTIPLY
                    "÷" -> DIVIDE
                    "=" -> EQUAL
                    "√" -> SQRT
                    "%" -> PERCENTAGE
                    "C" -> CLEAR
                    "AC" -> ALL_CLEAR
                    else -> EQUAL
                }
            }
        }
    }

    var operand = 0.0
    var operation: Operation? = null


    fun doOperation(newOperand: Double, newOperation: Operation) {
        when (newOperation) {
            Operation.ADD -> operand += newOperand
            Operation.SUBTRACT -> operand -= newOperand
            Operation.MULTIPLY -> operand *= newOperand
            Operation.DIVIDE -> operand /= newOperand

            Operation.SQRT -> operand = kotlin.math.sqrt(newOperand)
            Operation.PERCENTAGE -> operand = newOperand / 100.0

            Operation.ALL_CLEAR -> {
                operand = 0.0
                operation = null
            }

            Operation.CLEAR -> {
            }

            Operation.EQUAL -> {

            }
        }

        operation = newOperation
    }
}
