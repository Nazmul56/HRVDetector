package com.example.android.ui.fragment.bgTestHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.db.BgTestData
import com.example.android.db.DatabaseManager
import com.example.android.ui.adapter.BgAdapter
import com.example.android.utils.Lg
import com.example.android.camera2basic.R
import com.example.android.utils.EndlessRecyclerOnScrollListener

class BgTestHistoryFragment : Fragment(), BgAdapter.ContactsAdapterListener{

    var tagCode: String = BgTestHistoryFragment::class.java.simpleName
    private lateinit var bgTestHistoryViewModel: BgTestHistoryViewModel
    private var mAdapter: BgAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bgTestHistoryViewModel =
            ViewModelProvider(this).get(BgTestHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        bgTestHistoryViewModel.bgHistoryList = ArrayList<BgTestData>()

        mAdapter = context?.let { BgAdapter(it, bgTestHistoryViewModel.bgHistoryList as ArrayList<BgTestData>, this) }
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 36));
        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(mLayoutManager) {
           override fun onLoadMore(current_page: Int) {
                Lg.d("BG Test", "Scroll: $current_page")
                // do something...
                //UserViewModel.getInstant().apiRequest();
                //loadMore();
            }
        })
        recyclerView.adapter = mAdapter
 //       tvNoCallHistory.setVisibility(if (mAdapter.getItemCount() < 1) View.VISIBLE else View.GONE)
        observedRoomLiveData()
        return root
    }

    private fun observedRoomLiveData() {
        activity?.let {
            DatabaseManager(context).bgTestLiveList
                .observe(viewLifecycleOwner){ dataList: List<BgTestData> -> this.showAllUsers(dataList) }
        }
    }

    private fun showAllUsers(dataList: List<BgTestData>?) {
        if (dataList == null) //|| users.size() < 1)
            return
        bgTestHistoryViewModel.bgHistoryList?.clear()
        bgTestHistoryViewModel.bgHistoryList?.addAll(dataList)
        mAdapter?.notifyDataSetChanged()
    }

    override fun onContactSelected(callHistory: BgTestData?) {
       //To change body of created functions use File | Settings | File Templates.
    }
}