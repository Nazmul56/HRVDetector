package com.example.android.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.example.android.camera2basic.R
import java.text.SimpleDateFormat
import java.util.*

object D {
    private val TAG = "Dialog"//D::class.java.simpleName

    fun setProgressDialog(
        context: Context,
        progressText: String
    ): androidx.appcompat.app.AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = progressText
        /*  tvText.setTextColor(Color.parseColor("#000000"))
          tvText.textSize = 20f*/
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)
        val dialog = builder.create()
        dialog.show()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }

        return dialog
    }

    /**
     * Input Dialog need work here
     */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun showAddDialog(
        context: Context,
        myCallback: (bgLevel: Float, time: Long, unit: String, whenMeasure: String) -> Unit
    ) {
        val addDialog = Dialog(context, R.style.AppTheme)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(addDialog.window!!.attributes)
        addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        addDialog.setContentView(R.layout.dialog_add)
        addDialog.window!!.attributes = lp
        addDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        addDialog.window!!.setDimAmount(0.5f)
        addDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        addDialog.show()

        val list = C.WHEN_MEASURED_ALL_TYPE
        val adapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            list
        )

        val whenMeasureDropDown =
            addDialog.findViewById(R.id.filled_exposed_dropdown) as AutoCompleteTextView
        whenMeasureDropDown.setAdapter(adapter)

        val unitAdapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            C.unitType
        )

        val unitEditTextFilledExposedDropdown =
            addDialog.findViewById(R.id.filled_exposed_dropdown_unit) as AutoCompleteTextView
        unitEditTextFilledExposedDropdown.editableText.append(C.unitSelected)
        unitEditTextFilledExposedDropdown.setAdapter(unitAdapter)

        val dialogCancelButton = addDialog.findViewById(R.id.dialog_add_cancel) as TextView
        val dialogOkButton = addDialog.findViewById(R.id.dialog_add_add) as TextView
        val dialogAddDate = addDialog.findViewById(R.id.dialog_add_date) as TextView
        val dialogAddTime = addDialog.findViewById(R.id.dialog_add_time) as TextView
        val dialogAddBgConcentration =
            addDialog.findViewById(R.id.dialog_add_concentration) as EditText

        /** System Time in Long format  */
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        val simpleDateFormatForDate = SimpleDateFormat(C.datePattern)
        val currentDateStr = simpleDateFormatForDate.format(currentTime)
        dialogAddDate.text = currentDateStr

        val simpleDateFormatForTime = SimpleDateFormat(C.timePattern)
        val currentTimeStr = simpleDateFormatForTime.format(currentTime)
        dialogAddTime.text = currentTimeStr

        val startYear = calendar.get(Calendar.YEAR)
        val startMonth = calendar.get(Calendar.MONTH)
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = calendar.get(Calendar.MINUTE)

        var yearPicked = startYear
        var monthPicked = startMonth
        var dayPicked = startDay
        var hourPicked = startHour
        var minutePicked = startMinute

        dialogAddTime.setOnClickListener {
            pickTime(
                context, startHour, startMinute,
                C.is24HourFormat
            ) { hour, minute ->
                hourPicked = hour
                minutePicked = minute

                calendar.set(startYear, startMonth, startDay, hour, minute)
                dialogAddTime.text = simpleDateFormatForTime.format(calendar.time)
                Lg.d(TAG, "Hour: $hour Min: $minute")
            }
        }
        dialogAddDate.setOnClickListener {
            pickDate(
                context, startYear,
                startMonth, startDay
            ) { year, month, day ->
                yearPicked = year
                monthPicked = month //here month 0 is january
                dayPicked = day

                calendar.set(year, month, day)
                dialogAddDate.text = simpleDateFormatForDate.format(calendar.time)
                Lg.d(TAG, "Year: $year Month: $month Day: $day")
            }
        }

        dialogOkButton.setOnClickListener(View.OnClickListener {
            val bgLevel: String = dialogAddBgConcentration.text.toString()
            val bgLevelFloat = bgLevel.toFloatOrNull()
            //Make sure all the fields are filled with values
            if (TextUtils.isEmpty(dialogAddDate.text) ||
                TextUtils.isEmpty(dialogAddTime.text) ||
                TextUtils.isEmpty(bgLevel) ||
                TextUtils.isEmpty(whenMeasureDropDown.text)
            ) {

                Toast.makeText(context, "Please Enter All Value.", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            Lg.d(TAG, "bgLevel:" + bgLevel + "mealType:")
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(yearPicked, monthPicked, dayPicked, hourPicked, minutePicked)

            if (bgLevelFloat != null) {
                myCallback.invoke(
                    bgLevelFloat,
                    pickedDateTime.timeInMillis,
                    C.SELECTED_UNIT,
                    whenMeasureDropDown.text.toString()
                )
            }
            addDialog.dismiss()
        })
        dialogCancelButton.setOnClickListener { addDialog.dismiss() }
    }

    private fun pickDate(
        context: Context, startYear: Int, startMonth: Int, startDay: Int,
        myCallback: (year: Int, month: Int, day: Int) -> Unit
    ) {
        DatePickerDialog(context, { _, year, month, day ->
            myCallback.invoke(year, month, day)
        }, startYear, startMonth, startDay).show()
    }

    private fun pickTime(
        context: Context, startHour: Int, startMinute: Int, is24Hour: Boolean,
        myCallback: (hour: Int, minute: Int) -> Unit
    ) {
        TimePickerDialog(context, { _, hour, minute ->
            myCallback.invoke(hour, minute)
        }, startHour, startMinute, is24Hour).show()
    }
}