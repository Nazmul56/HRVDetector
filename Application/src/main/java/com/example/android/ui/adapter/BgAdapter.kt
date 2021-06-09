package com.example.android.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.camera2basic.R
import com.example.android.db.BgTestData
import com.example.android.ui.adapter.BgAdapter.MyViewHolder
import com.example.android.utils.C
import com.example.android.utils.Lg.d
import com.example.android.utils.U.isEmpty
import java.text.SimpleDateFormat
import java.util.*

/*
* Created by nazmul haque on 16/12/17.
*/
class BgAdapter(
    private val context: Context,
    private val bgTestHistoryList: MutableList<BgTestData>,
    private val listener: ContactsAdapterListener
) : RecyclerView.Adapter<MyViewHolder>(), Filterable {
    private var bgTestHistoryListFiltered: List<BgTestData>
    private val myUserName: String? = null
    private var searchString = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bg_test_history_row_item, parent, false)
        )
    }

    /**
     * Here Data add to model of recycler
     */
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val callHistoryItem = bgTestHistoryListFiltered[position]
        val timeInMilli = callHistoryItem.time
        val simpleDateFormatForDate = SimpleDateFormat(C.datePattern)
        holder.date.text = simpleDateFormatForDate.format(timeInMilli)
        val simpleDateFormatForTime = SimpleDateFormat(C.timePattern)

        holder.time.text = simpleDateFormatForTime.format(timeInMilli)
        holder.bgLevel.text = callHistoryItem.bgLevel.toString()
        holder.bgUnit.text = callHistoryItem.bgUnit
        holder.whenMeasured.text = callHistoryItem.whenMeasure
    }

    override fun getItemCount(): Int {
        return bgTestHistoryListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(sequence: CharSequence): FilterResults {
                if (sequence == null || isEmpty(sequence.toString())) {
                    bgTestHistoryListFiltered = bgTestHistoryList
                    searchString = ""
                } else {
                    val str = sequence.toString().toLowerCase()
                    searchString = str
                    val filteredList: MutableList<BgTestData> = ArrayList()
                    val filteredListForSecondName: List<BgTestData> = ArrayList()
                    for (row in bgTestHistoryList) {
//                        int pos = row.getThreadName().toLowerCase().indexOf(str);
//                        if (pos == 0)
//                            filteredList.add(row);
//                        else if (pos > 0)
//                            filteredListForSecondName.add(row);
                    }
                    filteredList.addAll(filteredListForSecondName)
                    bgTestHistoryListFiltered = filteredList
                    //callHistoryListFiltered.addAll(filteredList);
                }
                val filterResults = FilterResults()
                filterResults.values = bgTestHistoryListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                bgTestHistoryListFiltered = filterResults.values as ArrayList<BgTestData>
                notifyDataSetChanged()
            }
        }
    }

    fun replace(newList: ArrayList<BgTestData>) {
        bgTestHistoryList.clear()
        bgTestHistoryList.addAll(newList)
        notifyDataSetChanged()
    }

    interface ContactsAdapterListener {
        fun onContactSelected(callHistory: BgTestData?) //void onProfilePicSelected(CallHistory callHistory);
    }

    inner class MyViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var bgLevel: TextView = view.findViewById(R.id.tv_bg_test_level)
        var bgUnit: TextView = view.findViewById(R.id.tv_bg_test_unit)
        var date: TextView = view.findViewById(R.id.tv_bg_test_date)
        var time: TextView = view.findViewById(R.id.tv_bg_test_time)
        var whenMeasured: TextView = view.findViewById(R.id.tv_bg_test_when_measured)

        init {
            //Send to View Item Holder
            view.setOnClickListener {
                // send selected contact in callback
                val callHistory = bgTestHistoryListFiltered[adapterPosition]
                d(TAG, "Selected call history: $callHistory")
                listener.onContactSelected(callHistory)
            }
        }
    }

    companion object {
        private val TAG = BgAdapter::class.java.simpleName
    }

    /**
     * CallHistory Adapter
     *
     * @param context         activity context
     * @param callHistoryList call history list
     * @param listener        click listener
     */
    init {
        bgTestHistoryListFiltered = bgTestHistoryList
    }
}