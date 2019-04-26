package com.yanze.cloudreaderkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.yanze.cloudreaderkotlin.adapter.MyFragmentPageAdapter
import com.yanze.cloudreaderkotlin.app.ConstantsImageUrl
import com.yanze.cloudreaderkotlin.test.TextFragment
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.utils.statusbar.StatusBarUtil
import com.yanze.cloudreaderkotlin.view.movie.MovieFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initStatusView()
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this@MainActivity, drawer_layout,
                CommonUtils.getColor(R.color.colorTheme))

        initContentFragment()
        initDrawerLayout()
        initListener()
    }

//    fun initStatusView() {
//        view_status.layoutParams.height = StatusBarUtil.getStatusBarHeight(this@MainActivity)
//    }

    private fun initContentFragment() {
        val mFragmentList: ArrayList<Fragment> = ArrayList()
        mFragmentList.add(TextFragment())
        mFragmentList.add(TextFragment())
        mFragmentList.add(MovieFragment.getInstance())

        val adapter = MyFragmentPageAdapter(supportFragmentManager, mFragmentList)
        vp_content.adapter = adapter
        vp_content.offscreenPageLimit = 2
        vp_content.addOnPageChangeListener(this@MainActivity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setCurrentItem(0)
    }

    private fun initDrawerLayout() {
//        var view = layoutInflater.inflate(R.layout.nav_header_main, null, false)
        var view = nav_view.inflateHeaderView(R.layout.nav_header_main)
        val iv_avatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val ll_nav_exit = view.findViewById<LinearLayout>(R.id.ll_nav_exit)
        val ll_nav_homepage = view.findViewById<LinearLayout>(R.id.ll_nav_homepage)

        ImageLoadUtil.displayCircle(iv_avatar, ConstantsImageUrl.IC_AVATAR)
        ll_nav_exit.setOnClickListener(this)
        iv_avatar.setOnClickListener(this)

        //导航栏其他条目点击事件
        ll_nav_homepage.setOnClickListener(listener)
    }

    private fun initListener() {
        //init title bar
        ll_title_menu.setOnClickListener(this)
        iv_title_one.setOnClickListener(this)
        iv_title_two.setOnClickListener(this)
        iv_title_three.setOnClickListener(this)
    }

    /**
     * 切换界面
     */
    private fun setCurrentItem(position: Int) {
        var isOne = false
        var isTwo = false
        var isThree = false
        when (position) {
            0 -> isOne = true
            1 -> isTwo = true
            2 -> isThree = true
        }
        vp_content.currentItem = position
        iv_title_one.isSelected = isOne
        iv_title_two.isSelected = isTwo
        iv_title_three.isSelected = isThree
    }

    //防抖点击事件
    private val listener = object : PerfectClickListener() {
        override fun onNoDoubleClick(v: View?) {
            drawer_layout.closeDrawer(GravityCompat.START)
            drawer_layout.postDelayed({
                when (v?.id) {
                    (R.id.ll_nav_homepage) -> {
                        this@MainActivity.showToast("首页")
                    }
                }
            }, 260)
        }
    }

    //点击事件
    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.ll_title_menu) -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
            (R.id.iv_title_one) -> {
                if (vp_content.currentItem != 0) {
                    setCurrentItem(0)
                }
            }
            (R.id.iv_title_two) -> {
                if (vp_content.currentItem != 1) {
                    setCurrentItem(1)
                }
            }
            (R.id.iv_title_three) -> {
                if (vp_content.currentItem != 2) {
                    setCurrentItem(2)
                }
            }
            (R.id.iv_avatar) -> {
                this@MainActivity.showToast("头像进入Github")
            }
            (R.id.ll_nav_exit) -> {
                //退出应用
                finish()
            }
        }
    }

    /**
     * 获取剪贴板链接
     */
    private fun getClipContent() {
        var clipContent = BaseTools.getClipContent()
        if (!clipContent.isEmpty()) {
            this@MainActivity.showToast(clipContent)
        }
    }

    //搜索菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_search -> {
                this@MainActivity.showToast("搜索")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //viewpager切换监听
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> setCurrentItem(0)
            1 -> setCurrentItem(1)
            2 -> setCurrentItem(2)
        }
    }

    override fun onResume() {
        super.onResume()
        getClipContent()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
