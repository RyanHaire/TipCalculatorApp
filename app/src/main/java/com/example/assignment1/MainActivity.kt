package com.example.assignment1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.text.Format

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // input fields
        var amount = findViewById(R.id.editTextAmount) as EditText
        var tip = findViewById(R.id.editTextTipPercent) as EditText

        // drop down lists/spinners
        var spinnerTip = findViewById(R.id.ddlTip) as Spinner
        var spinnerNumPeople = findViewById(R.id.ddlNumPeople) as Spinner

        // calculate and clear buttons
        var btnCalculate = findViewById(R.id.btnCalculate) as Button
        var btnClear = findViewById(R.id.btnClear) as Button

        // text views for setting results from input fields
        var textViewTip = findViewById(R.id.textViewTip) as TextView
        var textViewTotal = findViewById(R.id.textViewTotal) as TextView
        var textViewPerPerson = findViewById(R.id.textViewPerPerson) as TextView

        // spinner information
        var tips = arrayOf("10%", "15%", "20%", "other")
        var numPeoples = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

        // loading spinners with information

        val spinnerArrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, tips)
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTip.adapter = spinnerArrayAdapter1


        val spinnerArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, numPeoples)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNumPeople.adapter = spinnerArrayAdapter


        // set onSelected handler for tip list
        spinnerTip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 3 && !tip.isEnabled) {
                    tip.isEnabled = true
                } else if (position != 3 && tip.isEnabled) {
                    tip.isEnabled = false
                    tip.text.clear()

                }
            }
        }

        // show or hide Per Person textview
        spinnerNumPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    textViewPerPerson.text = ""
                } else {
                    textViewPerPerson.text = "Per Person:"
                }
            }
        }
        // onClick event handler for calculate button
        btnCalculate.setOnClickListener {
            val amountTxt = amount.text.toString()
            val tipOption = spinnerTip.selectedItemPosition
            var tipSize: Double
            //option 3 of tip ddl is "other", checks tip amount and parses, set to -1 if invalid
            if (tipOption == 3) {
                val tipParse = tip.text.toString()
                if (tipParse == "" || tipParse.toDoubleOrNull() == null || tipParse.toDouble() < 0) {

                    tipSize = -1.0
                } else {
                    tipSize = tipParse.toDouble() / 100
                }
            } else {
                tipSize = 0.1 + (0.05 * tipOption)
            }
            //returns toast if amount or tip is invalid. Otherwise, calculates and sets text fields
            if (amountTxt.toDoubleOrNull() == null || tipSize < 0) {
                var toast = Toast.makeText(
                    this,
                    "At least one input is invalid. Please enter valid amounts.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                val amnt = amountTxt.toDouble()
                val tipAmnt = amnt * tipSize
                val totalAmnt = amnt + tipAmnt

                val splitPerson = totalAmnt / (spinnerNumPeople.selectedItemPosition + 1)

                textViewTip.text = "Tip is: $%.2f".format(tipAmnt)
                textViewTotal.text = "Total is: $%.2f".format(totalAmnt)
                if (spinnerNumPeople.selectedItemPosition > 0) {
                    textViewPerPerson.text = "Per Person: $%.2f".format(splitPerson)
                } else {
                    textViewPerPerson.text = ""
                }
            }

        }

        // onClick event handler for clear button
        btnClear.setOnClickListener {
            // .clear() clears the EditText field
            amount.text.clear()
            tip.text.clear()
            ddlTip.setSelection(0)
            tip.isEnabled = false
            ddlNumPeople.setSelection(0)
            textViewPerPerson.text = ""
            textViewTip.text = "Tip is: "
            textViewTotal.text = "Total is: "
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            // alert dialog with information about our app
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Tip Calculator")
            alertDialogBuilder.setMessage("Tip Calculator app made by Ryan Haire and Thomas Forber")
            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            }
            alertDialogBuilder.show()
        }
        return true
    }
}
