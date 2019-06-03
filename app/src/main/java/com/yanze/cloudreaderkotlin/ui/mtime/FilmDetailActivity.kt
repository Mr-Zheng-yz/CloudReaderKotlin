package com.yanze.cloudreaderkotlin.ui.mtime

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.FilmDetailActorAdapter
import com.yanze.cloudreaderkotlin.adapter.rv.FilmDetailImageAdapter
import com.yanze.cloudreaderkotlin.base.BaseHeaderActivity
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.FilmDetailResultMovie
import com.yanze.cloudreaderkotlin.data.bean.film.FilmDetailBasicBean
import com.yanze.cloudreaderkotlin.data.bean.film.FilmeItemBean
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_film_detail.view.*
import kotlinx.android.synthetic.main.header_film_detail.view.*

class FilmDetailActivity : BaseHeaderActivity() {

    private var filmItemBean: FilmeItemBean? = null
    private var mMoreUrl: String = ""
    private var mMoreTitle: String = ""
    private lateinit var viewModel: MtimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMTimeViewModelFactory(this@FilmDetailActivity))
                .get(MtimeViewModel::class.java)
        filmItemBean = intent.getSerializableExtra("bean") as FilmeItemBean?

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView()) //

        setTitle("${filmItemBean?.tCn}")
        setSubTitle("${filmItemBean?.tEn}")
        initHeaderView()
        initContentView()

        viewModel.setMovieId(filmItemBean?.id!!)
        mContentView.tv_one_title.postDelayed({ loadMovieDetail() }, 300)
    }

    private fun initContentView() {
    }

    @SuppressLint("SetTextI18n")
    private fun initHeaderView() {
        ImageLoadUtil.displayGaussFuzzy(mHeaderView.img_item_bg, "${filmItemBean?.img}")
        ImageLoadUtil.showMovieImg(mHeaderView.iv_one_photo, "${filmItemBean?.img}")
        mHeaderView.tv_one_rating_rate.text = "${getString(R.string.string_rating)}${filmItemBean?.r}}"
        mHeaderView.tv_one_directors.text = "${filmItemBean?.dN}"
        mHeaderView.tv_one_casts.text = "${filmItemBean?.actors}"
        mHeaderView.tv_one_genres.text = "${getString(R.string.string_type)}${filmItemBean?.movieType}"
    }

    override fun setHeaderLayout(): Int {
        return R.layout.header_film_detail
    }

    override fun setTitleClickMore() {
        if (mMoreUrl.isNotEmpty()) {
            WebViewActivity.loadUrl(this, mMoreUrl, mMoreTitle)
        } else {
            showToast("抱歉，暂无更多~")
        }
    }

    override fun setHeaderImgUrl(): String {
        if (filmItemBean == null) {
            return ""
        }
        return filmItemBean!!.img
    }

    override fun setHeaderImageView(): ImageView {
        return mHeaderView.img_item_bg
    }

    /**
     * 获取电影详情
     */
    private fun loadMovieDetail() {
        viewModel.getFilmDetail().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    showContentView()
                    updateUi(it.data!!)
                }
                Resource.ERROR -> {
                    showError()
                    showToast("${it.message}")
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(bean: FilmDetailResultMovie) {
        val basic = bean.data?.basic
        mHeaderView.tv_one_rating_rate.text = String.format("评分：%s", basic?.overallRating)
        mHeaderView.tv_one_rating_number.text = String.format(" %s人评分", basic?.personCount)
        mHeaderView.tv_one_date.text = "上映日期：${basic?.releaseDate} ${basic?.releaseArea}"
        mHeaderView.tv_one_time.text = "片长：${basic?.mins}"
        mContentView.tv_one_title.text = "“${basic?.commentSpecial}”"
        mContentView.tv_one_title.visibility = if (basic?.commentSpecial?.isEmpty()!!) View.GONE else View.VISIBLE
        mContentView.tv_film_story.text = basic.story

        //票房
        if (bean.data.boxOffice != null
                && bean.data.boxOffice.todayBoxDes.isNotEmpty()
                && bean.data.boxOffice.totalBoxDes.isNotEmpty()) {
            val boxOffice = bean.data.boxOffice
            mContentView.ll_boxoffice_tips.visibility = View.VISIBLE
            mContentView.ll_boxoffice_content.visibility = View.VISIBLE
            mContentView.tv_today_num.text = boxOffice.todayBoxDes
            mContentView.tv_today_unit.text = boxOffice.todayBoxDesUnit
            mContentView.tv_summary_num.text = boxOffice.totalBoxDes
            mContentView.tv_summary_unit.text = boxOffice.totalBoxUnit
            mContentView.tv_rank_num.text = "${boxOffice.ranking}"
        } else {
            mContentView.ll_boxoffice_tips.visibility = View.GONE
            mContentView.ll_boxoffice_content.visibility = View.GONE
        }

        //more
        if (bean.data.related != null && bean.data.related.relatedUrl.isNotEmpty()) {
            mMoreUrl = bean.data.related.relatedUrl
            mMoreTitle = "加载中..."
        }

        //导演&演员
        val actorAdapter = FilmDetailActorAdapter()
        mContentView.rv_film_cast.layoutManager = LinearLayoutManager(this@FilmDetailActivity
                , LinearLayoutManager.HORIZONTAL, false)
        actorAdapter.addAll(basic.actors)
        mContentView.rv_film_cast.adapter = actorAdapter
        mContentView.rv_film_cast.isNestedScrollingEnabled = false
        mContentView.rv_film_cast.setHasFixedSize(false)
        mContentView.rv_film_cast.isFocusable = false
        mContentView.rv_film_cast.isFocusableInTouchMode = false

        //剧照
        val imageAdapter = FilmDetailImageAdapter()
        imageAdapter.addAll(basic.stageImg.list)
        mContentView.rv_images.visibility = View.VISIBLE
        mContentView.rv_images.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        mContentView.rv_images.adapter = imageAdapter
        mContentView.rv_images.isNestedScrollingEnabled = false
        mContentView.rv_images.setHasFixedSize(false)
        mContentView.rv_images.isFocusableInTouchMode = false
        mContentView.rv_images.isFocusable = false

        //宣传片
        if (bean.data.basic.video != null && bean.data.basic.video.url.isNotEmpty()) {
            mContentView.ll_video_tips.visibility = View.VISIBLE
            mContentView.iv_video.visibility = View.VISIBLE
            val video = bean.data.basic.video
            DensityUtil.formatHeight(mContentView.iv_video, DensityUtil.getDisplayWidth() - DensityUtil.dip2px(40f), (640f / 360), 1)
            DensityUtil.setViewMargin(mContentView.iv_video, true, 20, 20, 10, 10);
            ImageLoadUtil.displayListImage(mContentView.iv_video,video.img,0)
            mContentView.iv_video.setOnClickListener { WebViewActivity.loadUrl(this,video.hightUrl,video.title) }
        } else {
            mContentView.ll_video_tips.visibility = View.GONE
            mContentView.iv_video.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        loadMovieDetail()
    }

    companion object {
        fun start(context: Activity, bean: FilmeItemBean, imageView: ImageView) {
            val intent = Intent(context, FilmDetailActivity::class.java)
            intent.putExtra("bean", bean)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, imageView, CommonUtils.getString(R.string.transition_movie_img))
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }

}
