package ipca.example.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipca.example.calculator.ui.theme.CalculatorTheme

@Composable
fun CalculatorView(
    modifier: Modifier = Modifier
){

    var displayText by remember { mutableStateOf("0") }

    val calculatorBrain by remember {  mutableStateOf(CalculatorBrain()) }

    var userIsTypingNumber by remember { mutableStateOf(true) }
    
    val onDigitPressed : (String) -> Unit = { digit ->
        if (userIsTypingNumber) {
            if (digit == ".") {
                if (!displayText.contains('.')) {
                    displayText += digit
                }
            } else {
                if (displayText == "0") {
                    displayText = digit
                } else {
                    displayText += digit
                }
            }
        }else{
            if (digit == ".") {
                displayText = "0."
            }else {
                displayText = digit
            }
        }
        userIsTypingNumber = true
    }


    val onOperationPressed: (String) -> Unit = { op ->
        when (op) {
            "C" -> {
                displayText = if (displayText.length > 1) displayText.dropLast(1) else "0"
            }
            "AC" -> {
                displayText = "0"
                calculatorBrain.doOperation(0.0, CalculatorBrain.Operation.ALL_CLEAR)
                userIsTypingNumber = false
            }
            else -> {
                val operation = CalculatorBrain.Operation.parseOperation(op)
                val currentValue = displayText.toDouble()

                when (operation) {
                    CalculatorBrain.Operation.SQRT -> {
                        val result = kotlin.math.sqrt(currentValue)
                        calculatorBrain.operand = result
                        displayText = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
                    }
                    CalculatorBrain.Operation.PERCENTAGE -> {
                        val result = currentValue / 100.0
                        calculatorBrain.operand = result
                        displayText = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
                    }
                    CalculatorBrain.Operation.EQUAL -> {
                        if (calculatorBrain.operation != null) {
                            val result = when (calculatorBrain.operation) {
                                CalculatorBrain.Operation.ADD -> calculatorBrain.operand + currentValue
                                CalculatorBrain.Operation.SUBTRACT -> calculatorBrain.operand - currentValue
                                CalculatorBrain.Operation.MULTIPLY -> calculatorBrain.operand * currentValue
                                CalculatorBrain.Operation.DIVIDE -> calculatorBrain.operand / currentValue
                                else -> currentValue
                            }
                            calculatorBrain.operand = result
                            displayText = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
                            calculatorBrain.operation = null
                        }
                    }
                    else -> {
                        // Operações básicas: +, -, ×, ÷
                        if (calculatorBrain.operation != null) {
                            val result = when (calculatorBrain.operation) {
                                CalculatorBrain.Operation.ADD -> calculatorBrain.operand + currentValue
                                CalculatorBrain.Operation.SUBTRACT -> calculatorBrain.operand - currentValue
                                CalculatorBrain.Operation.MULTIPLY -> calculatorBrain.operand * currentValue
                                CalculatorBrain.Operation.DIVIDE -> calculatorBrain.operand / currentValue
                                else -> currentValue
                            }
                            calculatorBrain.operand = result
                        } else {
                            calculatorBrain.operand = currentValue
                        }
                        calculatorBrain.operation = operation

                        val result = calculatorBrain.operand
                        displayText = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
                    }
                }
                userIsTypingNumber = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = displayText,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.displayLarge
        )
        Row() {
            CalculatorButton( label = "AC" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
            CalculatorButton( label = "C" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
            CalculatorButton( label = "√" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
            CalculatorButton( label = "%" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
        }
        Row() {
            CalculatorButton( label = "7" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "8" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "9" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "+" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
        }
        Row() {
            CalculatorButton( label = "6" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "5" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "4" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "-" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
        }
        Row() {
            CalculatorButton( label = "1" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "2" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "3" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "÷" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
        }
        Row() {
            CalculatorButton( label = "0" , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "." , onNumPressed =  onDigitPressed )
            CalculatorButton( label = "=" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
            CalculatorButton( label = "×" ,
                onNumPressed =  onOperationPressed,
                isOperation = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorViewPreview(){
    CalculatorTheme {
        CalculatorView()
    }
}