package com.example.android.ui.fragment.bgTestGraph

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.camera2basic.R

class GraphFragment : Fragment() {
    private val patentPref = "PatientPrefs"
    private lateinit var spinnerGraphTimeRange: Spinner
    private lateinit var spinnerGraphMealRange: Spinner
    private var unitKey = "key_unit"

    private lateinit var homeViewModel: GraphViewModel
    fun newInstance(): GraphFragment {
        val args = Bundle()
        val fragment = GraphFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(GraphViewModel::class.java)

        val rootView2 = inflater.inflate(R.layout.fragment_graph, container, false)
/*        val lineChart = rootView2.findViewById(R.id.chart) as LineChart
        val mPieChart = rootView2.findViewById(R.id.pi_chart1) as PieChart

        val bgUnitStr = C.SELECTED_UNIT


        *//** Show Unit in Graph left side vertically *//*
        val graphTv = rootView2.findViewById(R.id.graph_unit) as TextView
        //graphTv.text = bgUnitStr

        *//** init variable for x and y*//*
        val xVals = ArrayList<String>()
        val yVals = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        *//** Set Sample Data*//*
        val date = "2019FEB12:30PM"
        val reading = java.lang.Float.parseFloat("200")
        xVals.add(date)  //time in x axis
        //TODO need get data from database
        *//** Prepared demo random data*//*
        val count = 30 // as month day
        val range = 2
        for (i in 0 until count) {
            val value = (Math.random() * (range / 2f)).toFloat() + 50
            yVals.add(Entry(i.toFloat(), value))
        }
        //yVals.add(Entry(reading, 1f)) // intensity of glucose in y axis
        *//** Graph node color *//*
        val colorCode = context?.let { GraphHelper.getGraphPointColor(it, bgUnitStr, reading) }
        if (colorCode != null) {
            colors.add(colorCode)
        }

        *//** Set Line Chart Data *//*
        setLineChartDataUi(lineChart, xVals, yVals, colors)
        *//** Last Reading  get Data From Shared Preferences *//*
        setLastTestInUi(rootView2)
        *//** Set Circle Pie Chart *//*
        setPieChartData(mPieChart)
        *//** Week , Day , Month List Selector *//*
        setTimeRangeSpinner(rootView2)
        *//** After Breakfast , Before Breakfast *//*
        setMealRangeSpinner(rootView2)*/
        return rootView2
    }

/*    private fun setLineChartDataUi(
        lineChart: LineChart,
        xVals: ArrayList<String>,
        yVals: ArrayList<Entry>,
        colors: ArrayList<Int>
    ) {

        //X-Axis Config
        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = resources.getColor(R.color.glucosio_text_light)
        xAxis.setAvoidFirstLastClipping(true)
        lineChart.axisRight.isEnabled = false
        lineChart.setBackgroundColor(Color.parseColor("#FFFFFF")) //White Background color
        lineChart.setGridBackgroundColor(Color.parseColor("#FFFFFF"))// White grid

        //Data Set from y value
        val set1 = LineDataSet(yVals, "Y value")
        // set the line to be drawn like this "- - - - - -"
//        set1.color = resources.getColor(R.color.glucosio_pink) //Whole Connection line Color
        set1.circleColors = colors
        set1.lineWidth = 1f// graph line width
        set1.circleRadius = 2.8f
        set1.setDrawCircleHole(false)//Circle hole
        set1.disableDashedLine()
        set1.fillAlpha = 255
        set1.setDrawFilled(true)
        set1.valueTextSize = 8f// Each Item Value Test Size
        set1.valueTextColor = Color.parseColor("#000000") //Black Text Color
        set1.fillColor = Color.parseColor("#FCE2EA")
        colors.add(resources.getColor(R.color.reading_low))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            set1.setDrawFilled(false)
            set1.lineWidth = 3f
            set1.circleRadius = 4.5f
            set1.setDrawCircleHole(true)
        }

        *//** add the datasets in graph *//*
        val lineChartData = LineData(set1)
        lineChart.data = lineChartData
        lineChart.setPinchZoom(true)
        lineChart.setHardwareAccelerationEnabled(true)
        val easingFunction = Easing.EaseOutCubic
        lineChart.animateY(500, easingFunction)

    }*/

    private fun setLastTestInUi(rootView2: View) {
        //TODO Last data should get using query
        val tvLastGlucoseTest = rootView2.findViewById(R.id.last_reading) as TextView
        tvLastGlucoseTest.text = "Last test Value and date"
    }

/*    private fun setPieChartData(mPieChart: PieChart) {
        mPieChart.setDrawSliceText(true)
        mPieChart.setUsePercentValues(true)
        val description = Description()
        description.text = "Pie Chart"
        description.textSize = 25f
        mPieChart.setExtraOffsets(10f, 10f, 5f, 5f)
        mPieChart.dragDecelerationFrictionCoef = 0.10f
        //mPieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        //mPieChart.setCenterText(generateCenterSpannableText());
        mPieChart.centerText = "Test Frequency"
        mPieChart.isDrawHoleEnabled = true
        mPieChart.setHoleColor(Color.WHITE)
        mPieChart.setTransparentCircleColor(Color.GREEN)
        mPieChart.setTransparentCircleAlpha(100)
        mPieChart.holeRadius = 58f
        mPieChart.transparentCircleRadius = 58f
        mPieChart.setDrawCenterText(true)
        mPieChart.rotationAngle = 10f
        // enable rotation of the chart by touch
        mPieChart.isRotationEnabled = false
        mPieChart.isHighlightPerTapEnabled = true
        //mPieChart.setOnChartValueSelectedListener(getContext());
        *//***Clear Graph*//*
        GraphHelper.setData(mPieChart, 4, 100f)
        val easingFunction = Easing.EaseInOutBounce

        mPieChart.animateY(1400, easingFunction)
        //mPieChart.spin(2000, 0, 360);

        val l = mPieChart.legend
//        l.position = Legend.LegendPosition.ABOVE_CHART_LEFT
        l.textSize = 15f
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
    }*/

    private fun setTimeRangeSpinner(rootView2: View) {
        spinnerGraphTimeRange = rootView2.findViewById(R.id.chart_spinner)
        spinnerGraphTimeRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>, v: View,
                position: Int, id: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }
    }

    private fun setMealRangeSpinner(rootView2: View) {
        spinnerGraphMealRange = rootView2.findViewById(R.id.spinner_meal) as Spinner
        spinnerGraphMealRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>, v: View,
                position: Int, id: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }
    }
}