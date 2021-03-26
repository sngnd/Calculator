//package com.example.calculator
//
//import android.util.Log
//import android.widget.Toast
//import java.util.*
//
//public class ReversePolishNotation(line: String) {
//    private var stack = Stack<Char>()
//    private var numberStack = Stack<String>()
//    private var calculateStack = Stack<Double>()
//    private var line: String = line
//    private var output: String = ""
//
//    private fun transform() {
//        var number = ""
//        line.forEach {
//            when (it) {
//                '+', '-' -> {
//                    onOperation(it, 1)
//                        numberStack.push(number)
//                        number = ""
//                }
//                '*', '/' -> {
//                    onOperation(it, 2)
//                        numberStack.push(number)
//                        number = ""
//                }
//                '(' -> {
//                    stack.push(it)
//                }
//                ')' -> {
//                    onBracket()
//                }
//                else -> {
//                    number += it
//                }
//            }
//        }
//        if (number.isNotEmpty()) numberStack.push(number)
//        while (!stack.isEmpty()) output += stack.pop();
//    }
//
//    private fun onOperation(op: Char, priority: Int) {
//        while (!stack.isEmpty()) {
//            val onTop = stack.pop()
//            if (onTop == '(') {
//                stack.push(onTop)
//                break
//            } else {
//                var oldPriority: Int = if (onTop == '+' || onTop == '-') 1
//                else 2
//
//                if (oldPriority < priority) {
//                    stack.push(onTop)
//                    break
//                } else output += onTop
//            }
//        }
//        stack.push(op)
//    }
//
//    private fun onBracket() {
//        while (!stack.isEmpty()) {
//            var elem = stack.pop()
//            if (elem == '(') break
//            else output += elem
//        }
//    }
//
//    public fun getPolishNotation(): String {
//        transform()
//        return output
//    }
//}
//
