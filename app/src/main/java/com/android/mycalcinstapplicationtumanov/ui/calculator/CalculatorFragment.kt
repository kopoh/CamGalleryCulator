package com.android.mycalcinstapplicationtumanov.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.mycalcinstapplicationtumanov.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {

    private var _binding : FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        val calculatorViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root : View = binding.root

        val SecondaryText : TextView = binding.idTVSecondary
        calculatorViewModel.textSeconday.observe(viewLifecycleOwner) {
            SecondaryText.text = it.toString()
        }
        val PrimaryText : TextView = binding.idTVprimary
        calculatorViewModel.textPrimary.observe(viewLifecycleOwner) {
            PrimaryText.text = it.toString()
        }
        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calculatorViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)

        val tvsec : TextView = binding.idTVSecondary
        val tvMain : TextView = binding.idTVprimary
        val bac : Button = binding.bac
        val bc : Button = binding.bc
        val bbrac1 : Button = binding.bbrac1
        val bbrac2 : Button = binding.bbrac2
        val bsin : Button = binding.bsin
        val bcos : Button = binding.bcos
        val btan : Button = binding.btan
        val blog : Button = binding.blog
        val bln : Button = binding.bln
        val bfact : Button = binding.bfact
        val bsquare : Button = binding.bsquare
        val bsqrt : Button = binding.bsqrt
        val binv : Button = binding.binv
        val b0 : Button = binding.b0
        val b9 : Button = binding.b9
        val b8 : Button = binding.b8
        val b7 : Button = binding.b7
        val b6 : Button = binding.b6
        val b5 : Button = binding.b5
        val b4 : Button = binding.b4
        val b3 : Button = binding.b3
        val b2 : Button = binding.b2
        val b1 : Button = binding.b1
        val bpi : Button = binding.bpi
        val bmul : Button = binding.bmul
        val bminus : Button = binding.bminus
        val bplus : Button = binding.bplus
        val bequal : Button = binding.bequal
        val bdot : Button = binding.bdot
        val bdiv : Button = binding.bdiv

        // adding on click listener to our all buttons.
        b1.setOnClickListener {
            // on below line we are appending
            // the expression to our text view.
            tvMain.text = (tvMain.text.toString() + "1")
        }
        b2.setOnClickListener {
            // on below line we are appending
            // the expression to our text view.
            tvMain.text = (tvMain.text.toString() + "2")
        }
        b3.setOnClickListener {
            // on below line we are appending
            // the expression to our text view.
            tvMain.text = (tvMain.text.toString() + "3")
        }
        b4.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "4")
        }
        b5.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "5")
        }
        b6.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "6")
        }
        b7.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "7")
        }
        b8.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "8")
        }
        b9.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "9")
        }
        b0.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "0")
        }
        bdot.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + ".")
        }
        bplus.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "+")
        }
        bdiv.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "/")
        }
        bbrac1.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "(")
        }
        bbrac2.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + ")")
        }
        bpi.setOnClickListener {
            // on clicking on pi button we are adding
            // pi value as 3.142 to our current value.
            tvMain.text = (tvMain.text.toString() + "3.142")
            tvsec.text = (bpi.text.toString())
        }
        bsin.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "sin")
        }
        bcos.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "cos")
        }
        btan.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "tan")
        }
        binv.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "^" + "(-1)")
        }
        bln.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "ln")
        }
        blog.setOnClickListener {
            tvMain.text = (tvMain.text.toString() + "log")
        }

        bminus.setOnClickListener {
            // on clicking on minus we are checking if
            // the user has already a minus operation on screen.
            // if minus operation is already present
            // then we will not do anything.
            val str : String = tvMain.text.toString()
            if (!str.get(index = str.length - 1).equals("-")) {
                tvMain.text = (tvMain.text.toString() + "-")
            }
        }
        bmul.setOnClickListener {
            // if mul sign is not present in our
            // text view then only we are adding
            // the multiplication operator to it.
            val str : String = tvMain.text.toString()
            if (!str.get(index = str.length - 1).equals("*")) {
                tvMain.text = (tvMain.text.toString() + "*")
            }
        }
        bsqrt.setOnClickListener {
            if (tvMain.text.toString().isEmpty()) {
                // if the entered number is empty we are displaying an error message.
                Toast.makeText(getActivity(), "Please enter a valid number..", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val str : String = tvMain.text.toString()
                // on below line we are calculation
                // square root of the given number.
                val r = Math.sqrt(str.toDouble())
                // on below line we are converting our double
                // to string and then setting it to text view.
                val result = r.toString()
                tvMain.setText(result)
            }
        }
        bequal.setOnClickListener {
            val str : String = tvMain.text.toString()
            // on below line we are calling an evaluate
            // method to calculate the value of expressions.
            val result : Double = evaluate(str)
            // on below line we are getting result
            // and setting it to text view.
            val r = result.toString()
            calculatorViewModel.primary.value = r
            tvMain.setText(r)
            calculatorViewModel.secndary.value = str
            tvsec.text = str
        }
        bac.setOnClickListener {
            // on clicking on ac button we are clearing
            // our primary and secondary text view.
            tvMain.setText("")
            tvsec.setText("")
        }
        bc.setOnClickListener {
            // on clicking on c button we are clearing
            // the last character by checking the length.
            var str : String = tvMain.text.toString()
            if (!str.equals("")) {
                str = str.substring(0, str.length - 1)
                tvMain.text = str
            }
        }
        bsquare.setOnClickListener {
            if (tvMain.text.toString().isEmpty()) {
                // if the entered number is empty we are displaying an error message.
                Toast.makeText(getActivity(), "Please enter a valid number..", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // on below line we are getting the expression and then calculating the square of the number
                val d : Double = tvMain.getText().toString().toDouble()
                // on below line we are calculating the square.
                val square = d * d
                // after calculating the square we
                // are setting it to text view.
                tvMain.setText(square.toString())
                // on below line we are setting
                // the d to secondary text view.
                tvsec.text = "$d²"
            }
        }
        bfact.setOnClickListener {
            if (tvMain.text.toString().isEmpty()) {
                // if the entered number is empty we are displaying an error message.
                Toast.makeText(getActivity(), "Please enter a valid number..", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // on below line we are getting int value
                // and calculating the factorial value of the entered number.
                val value : Int = tvMain.text.toString().toInt()
                val fact : Int = factorial(value)
                tvMain.setText(fact.toString())
                tvsec.text = "$value`!"
            }

        }

    }

    fun factorial(n : Int) : Int {
        // this method is use to find factorial
        return if (n == 1 || n == 0) 1 else n * factorial(n - 1)
    }

    fun evaluate(str : String) : Double {
        return object : Any() {
            // on below line we ar creating variable
            // for tracking the position and char pos.
            var pos = -1
            var ch = 0

            // below method is for moving to next character.
            fun nextChar() {
                // on below line we are incrementing our position
                // and moving it to next position.
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            // this method is use to check the extra space
            // present int the expression and removing it.
            fun eat(charToEat : Int) : Boolean {
                while (ch == ' '.toInt()) nextChar()
                // on below line we are checking the char pos
                // if both is equal then we are returning it to true.
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            // below method is to parse our
            // expression and to get the ans
            // in this we are calling a parse
            // expression method to calculate the value.
            fun parse() : Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) return 0.0//throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // in this method we will only perform addition and
            // subtraction operation on the expression.
            fun parseExpression() : Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            // in below method we will perform
            // only multiplication and division operation.
            fun parseTerm() : Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            // below method is use to parse the factor
            fun parseFactor() : Double {
                //on below line we are checking for addition
                // and subtraction and performing unary operations.
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus
                // creating a double variable for ans.
                var x : Double
                // on below line we are creating
                // a variable for position.
                val startPos = pos
                // on below line we are checking
                // for opening and closing parenthesis.
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                    // numbers
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    // on below line we are getting sub string from our string using start and pos.
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.toInt() && ch <= 'z'.toInt()) {
                    // on below function we are checking for the operator in our expression.
                    while (ch >= 'a'.toInt() && ch <= 'z'.toInt()) nextChar()
                    val func = str.substring(startPos, pos)
                    // calling a method to parse our factor.
                    x = parseFactor()
                    // on below line we are checking for square root.
                    x =
                        if (func == "sqrt") Math.sqrt(x)
                        // on below line we are checking for sin function
                        // and calculating sin function using Math class.
                        else if (func == "sin") Math.sin(
                            Math.toRadians(x)
                            // on below line we are calculating the cos value
                        ) else if (func == "cos") Math.cos(
                            Math.toRadians(x)
                            // on below line we are calculating
                            // the tan value of our expression.
                        ) else if (func == "tan")
                            Math.tan(Math.toRadians(x))
                        // on below line we are calculating
                        // log value of the expression.
                        else if (func == "log")
                            Math.log10(x)
                        // on below line we are calculating
                        // ln value of expression.
                        else Math.log(x)
                    // f we get any error then
                    // we simply return the exception.

                } else {
                    // if the condition not satisfy then we are returning the exception
                    Toast.makeText(getActivity(), "Unexpected: " + ch.toChar(), Toast.LENGTH_SHORT)
                        .show()
                    return 0.0

                }
                // on below line we are calculating the power of the expression.
                if (eat('^'.toInt())) x = Math.pow(x, parseFactor()) // exponentiation
                return x
            }
            // at last calling a parse for our expression.
        }.parse()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}