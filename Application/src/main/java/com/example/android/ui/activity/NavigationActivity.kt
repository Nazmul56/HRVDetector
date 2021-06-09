package com.example.android.ui.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.android.camera2basic.*
import com.example.android.ui.fragment.bgTestGraph.GraphFragment
import com.example.android.ui.fragment.bgTestHistory.BgTestHistoryFragment
import com.example.android.ui.fragment.reminder.ReminderFragment
import com.example.android.ui.viewmodel.NavigationViewModel
import com.example.android.utils.Lg
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_navigation_view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * The type Navigation activity.
 */
class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * The Navigation view model.
     */
    private lateinit var navigationViewModel: NavigationViewModel
    private lateinit var context: Context
    /**
     * The Is cookie already verified.
     */
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    /**
     * To hold the selected country-position from the custom-contact-add dialog.
     */
    private var pagerAdapter: ViewPagerAdapter? = null
    private var searchView: SearchView? = null
    override fun getLayoutResId() = R.layout.activity_navigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@NavigationActivity
        //initialize this for without open chatActivity make ready chatViewModel for cache.
        navigationViewModel = ViewModelProvider(this).get(NavigationViewModel::class.java)
        navigationViewModel.setLiveContactListSync(false)
        configDrawer()
        initNavigationActivity()
        setUpFabDialClickListener()

        if (Intent.ACTION_SEARCH == intent.action)
            handleSearchIntent(intent)
    }

    private fun configDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar);
        //toolbar = findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(R.drawable.ic_app_light);
        toolbar.setTitleMargin(0, 0, 0, 0)
        setSupportActionBar(toolbar)
        /*if (getSupportActionBar() != null)
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);*/
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        var drawerStr = "not null"
        if (drawer == null) {
            drawerStr = "null"
        }

        Lg.d(TAG, "Drawer layout :  $drawerStr")
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        setDrawerProfileInfo(headerView)
        //Navigation drawer Header Edit Button
//        headerView.findViewById<View>(R.id.btnEditProfileDrawer).setOnClickListener { v ->
//            drawer.closeDrawer(navigationView)
//            val intent = Intent(this@NavigationActivity, OwnProfileActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDrawerProfileInfo(headerView: View) {

    }

    private fun setUpFabDialClickListener() {
        fabAddMsg.setOnClickListener {
            /*D.showAddDialog(this) { bgLevel, time, unit, whenMeasure ->
                Lg.d(TAG, "bgLevel:" + bgLevel + "mealType:")
                val data = BgTestData(bgLevel, unit, time, whenMeasure )
                DatabaseManager(context).addBgTestData(data)
            }*/
            detectHeartRate(it)

        }
      //  fabDial.setOnClickListener { }
        fabCreateGroup.setOnClickListener {
            viewHistory(it)
        }
    }

    fun detectHeartRate(view: View?) {
        val intent = Intent(applicationContext, InstructionActivity::class.java)
        startActivityForResult(intent, 3)
    }

    fun viewHistory(view: View?) {
        val intent = Intent(applicationContext, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun startDetectHeartRate() {
        val intent = Intent(applicationContext, CameraActivity::class.java)
        startActivityForResult(intent, 1)
    }

    fun calculateHeartRate() {
        val intent = Intent(applicationContext, ResultsActivity::class.java)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                calculateHeartRate()
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                startDetectHeartRate()
            }
        }
    }

    private fun initNavigationActivity() {
        setupViewPager()

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) =
                configFabs(tab.position, fabAddMsg, /*fabDial,*/ fabCreateGroup)

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        setCurrentPage(0)
        configFabs(0, fabAddMsg, /*fabDial,*/ fabCreateGroup)
    }

    private fun configFabs(
        position: Int = tabLayout!!.selectedTabPosition,
        fabContactAdd: FloatingActionButton = findViewById(R.id.fabAddMsg),
       /* fabDial: FloatingActionButton = findViewById(R.id.fabDial)*/
        fabCreateGroup: FloatingActionButton = findViewById(R.id.fabCreateGroup)
    ) {
        if (position == 0 && !fabContactAdd.isShown)
            fabContactAdd.show()
        else if (fabContactAdd.isShown)
            fabContactAdd.hide()

        if (position != 1) { // 2 index of last fragment
           /* if (!fabDial.isShown)
                fabDial.show()*/
            fabCreateGroup.hide()
        } else {
            if (!fabCreateGroup.isShown)
                fabCreateGroup.show()
            /*fabDial.hide()*/
        }
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewpager)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFrag(
            GraphFragment(),
            "Messages",
            R.drawable.ic_timeline_24dp
        )
        pagerAdapter!!.addFrag(
            BgTestHistoryFragment(),
            "History",
            R.drawable.ic_assignment_24dp
        )
        pagerAdapter!!.addFrag(
            ReminderFragment(),
            "Contacts",
            R.drawable.ic_history_24dp
        )
//        pagerAdapter!!.addFrag(ShareFragment(),
//                "Groups",
//                R.drawable.ic_group_black_24dp)
        viewPager!!.adapter = pagerAdapter
        //Set 4 fragment cache not refresh first and last
        viewPager!!.offscreenPageLimit = 3

        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)
        val tc = tabLayout!!.tabCount
        for (i in 0 until tc) {
            val tab = tabLayout!!.getTabAt(i)
            if (tab != null)
                tab.customView =
                        getTabView(pagerAdapter!!.getPageTitle(i), pagerAdapter!!.getTabIconId(i))
        }
    }

    private fun getTabView(title: CharSequence?, iconResId: Int): View {
        val v = LayoutInflater.from(this@NavigationActivity).inflate(R.layout.tab_item, null, false)
        (v.findViewById<View>(R.id.tvTitleTab) as TextView).text = title
        (v.findViewById<View>(R.id.ivIconTab) as ImageView).setImageResource(iconResId)
        return v
    }

    /**
     * Sets current page.
     *
     * @param pageNo the page no
     */
    private fun setCurrentPage(pageNo: Int) {
        if (pageNo > -1 && pageNo < pagerAdapter!!.count)
            viewPager!!.currentItem = pageNo
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nav_with_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //doSearch(query)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                //doSearch(s)
                return true
            }
        })
        searchView!!.setOnCloseListener {
            Lg.i(TAG, "Search Closed")
            true
        }
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (sm != null)
            searchView!!.setSearchableInfo(sm.getSearchableInfo(componentName))
        return true
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        configFabs()
        setDrawerProfileInfo(findViewById<NavigationView>(R.id.nav_view).getHeaderView(0))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action)
            handleSearchIntent(intent)
    }

    private fun handleSearchIntent(intent: Intent) {
        val query = intent.getStringExtra(SearchManager.QUERY)
        if (pagerAdapter == null) {
            // if in any case any search launches this activity & this function is called prior to
            // the adapter init, then wait a while before continuing
            Handler().postDelayed({ handleSearchIntent(intent) }, 150)
            return
        }
        //doSearch(query)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        val id = item.itemId
        when (id) {
//            R.id.nav_invite_friend -> U.shareApp(this)
            R.id.nav_settings -> {
                val intent = Intent(this@NavigationActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_about -> {
                val intent = Intent(this@NavigationActivity, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private inner class ViewPagerAdapter
    /**
     * Instantiates a new View pager adapter.
     *
     * @param manager the manager
     */
    internal constructor(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()
        private val tabIconList = ArrayList<Int>()
        /**
         * Is filtered boolean.
         * @return the boolean
         * Sets filtered.
         */
        internal var isFiltered = false

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        /**
         * Add frag.
         *
         * @param fragment the fragment
         * @param title    the title
         * @param iconId   the icon id
         */
        internal fun addFrag(fragment: Fragment, title: String, @DrawableRes iconId: Int) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
            tabIconList.add(iconId)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        /**
         * Gets tab icon id.
         *
         * @param position the position
         * @return the tab icon id
         */
        internal fun getTabIconId(position: Int): Int {
            return tabIconList[position % tabIconList.size]
        }
    }

    companion object {
        private val TAG = NavigationActivity::class.java.simpleName
    }
}