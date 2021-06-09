package com.example.android.ui.fragment.bgTestGraph

import android.content.Context
import android.graphics.Color
import android.os.Build
import com.example.android.camera2basic.R

import com.example.android.utils.C
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.ArrayList

object GraphHelper {

    fun getGraphPointColor(context: Context, bgUnit: String, reading: Float): Int{

        var colorCode: Int = getColorCode(context, R.color.reading_hypo)
        if (bgUnit == C.UNIT.MGM_PER_DL) {
            //Unit: mg/dL
            when {
                reading < 54 -> //50 to 54 mg/dL
                    colorCode = getColorCode(context, R.color.reading_hypo)
                reading < 70 -> colorCode = getColorCode(context, R.color.reading_low)
                (reading >= 70) and (reading <= 180) -> colorCode = getColorCode(context, R.color.reading_ok)
                (reading > 180) and (reading < 200) -> colorCode = getColorCode(context, R.color.reading_high)
                reading >= 200 -> colorCode = getColorCode(context, R.color.reading_hyper)
            }
        } else if (bgUnit == C.UNIT.MMOLE_PER_DL) {
            //Unit: mmol/dL
            val readingMmolePdl = reading*18.01801801801802.toFloat()
            when {
                readingMmolePdl < 54 -> //50 to 54 mg/dL
                    colorCode = getColorCode(context, R.color.reading_hypo)
                readingMmolePdl < 70 -> colorCode = getColorCode(context, R.color.reading_low)
                (readingMmolePdl >= 70) and (readingMmolePdl <= 180) -> colorCode = getColorCode(context, R.color.reading_ok)
                (readingMmolePdl > 180) and (readingMmolePdl < 200) -> colorCode = getColorCode(context, R.color.reading_high)
                readingMmolePdl >= 200 -> colorCode = getColorCode(context, R.color.reading_hyper)
            }
        }
        return colorCode
    }

    fun getColorCode(context: Context, colorIntXmlResId:Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(colorIntXmlResId)
        } else {
            context.resources.getColor(colorIntXmlResId)
        }
    }

    fun printDifference(startDate: Long, endDate: Long): String {
        var result = ""
        var different = endDate - startDate

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        var ago = 0
        //  System.out.printf("AboutsActivity ");
        when {
            elapsedDays != 0L -> {
                result = when {
                    elapsedDays > 365 -> {
                        //System.out.printf(
                        //      "%d years ",(int) elapsedDays/365);
                        (elapsedDays.toInt() / 365).toString() + " years"
                    }
                    elapsedDays > 30 -> {
                        //System.out.printf(
                        // "%d years ",(int) elapsedDays/30);
                        (elapsedDays.toInt() / 30).toString() + " months"
                    }
                    else -> {
                        // System.out.printf("%d days ", elapsedDays);
                        "$elapsedDays days"
                    }
                }
                ago = 1
            }
            elapsedHours != 0L -> {
                //System.out.printf("%d hours ",elapsedHours);
                result = "$elapsedHours hours"
                ago = 1
            }
            elapsedMinutes != 0L -> {
                //System.out.printf("%d minutes ", elapsedMinutes);
                result = "$elapsedMinutes minutes"
                ago = 1
            }
            elapsedSeconds != 0L -> {
                //System.out.printf("%d seconds ", elapsedSeconds);
                result = "$elapsedSeconds seconds"
                ago = 1
            }
        }
        result = if (ago == 1) {
            // System.out.printf("ago\n");
            "$result ago"
        } else {
            //System.out.printf("0 sec ");
            "0 sec ago "
        }
        return result
    }

    fun setData(mPieChart: PieChart, count: Int, range: Float) {

        val mult = range
        val xVals = ArrayList<String>()
        val yVals1 = ArrayList<Entry>()
        val values1 = ArrayList<PieEntry>()

        for (i in 0 until count) {
            val `val` = (Math.random() * (range / 2f)).toFloat() + 50
            //values1.add(Entry(i.toFloat(), `val`))
            val pieEntry = PieEntry(i.toFloat())
            values1.add(pieEntry)

        }

        val mParties = arrayOf(
            "Breakfast",
            "Lunch",
            "Dinner",
            "Other"
        )
        for (i in 0 until count)
            xVals.add(mParties[i % mParties.size])

        val dataSet = PieDataSet(values1,"Pie")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // add a lot of colors

        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.DKGRAY)
        //data.setValueTypeface(tf);
        mPieChart.data = data
        // undo all highlights
        mPieChart.highlightValues(null)
        mPieChart.invalidate()
    }
}