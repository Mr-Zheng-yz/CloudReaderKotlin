package com.yanze.cloudreaderkotlin.ui.movie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.MovieDetailAdapter
import com.yanze.cloudreaderkotlin.base.BaseHeaderActivity
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.MovieDetailBean
import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.header_slide_shape.view.*

@Deprecated("豆瓣API已关闭...")
class MovieDetailActivity : BaseHeaderActivity() {

    private var subjectBean: SubjectsBean? = null
    private lateinit var viewModel: MovieViewModel
    private var mMoreUrl: String? = null
    private var mMovieName: String? = null
    private lateinit var adapter: MovieDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMovieFactory(this))
                .get(MovieViewModel::class.java)
        if (intent != null) {
            subjectBean = intent.getSerializableExtra("bean") as SubjectsBean?
        }

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView())

        setTitle("${subjectBean?.title}")
        setSubTitle(String.format("主演：%s", StringFormatUtil.formatName(subjectBean?.casts)))

        initHeaderView()
        initContentView()

        loadMovieDetail()
    }

    private fun initHeaderView() {
        ImageLoadUtil.displayGaussFuzzy(mHeaderView.img_item_bg, "${subjectBean?.images?.medium}")
        ImageLoadUtil.showMovieImg(mHeaderView.iv_movie_photo, "${subjectBean?.images?.large}")
        mHeaderView.tv_movie_rating_rate.text = "${getString(R.string.string_rating)}${subjectBean?.rating?.average}"
        mHeaderView.tv_movie_rating_number.text = "${subjectBean?.collect_count}${getString(R.string.string_rating_num)}"
        mHeaderView.tv_movie_directors.text = "${StringFormatUtil.formatName(subjectBean?.directors)}"
        mHeaderView.tv_movie_casts.text = "${StringFormatUtil.formatName(subjectBean?.casts)}"
        mHeaderView.tv_movie_genres.text = "${getString(R.string.string_type)}${StringFormatUtil.formatGenres(subjectBean?.genres)}"
    }

    private fun initContentView() {
        adapter = MovieDetailAdapter()
        xrv_cast.layoutManager = LinearLayoutManager(this)
        xrv_cast.setPullRefreshEnabled(false)
        xrv_cast.setLoadingMoreEnabled(false)

        //RecyclerView不需要滑动，交给NestView去滑动
        xrv_cast.isNestedScrollingEnabled = false
        xrv_cast.setHasFixedSize(false)

        xrv_cast.adapter = adapter
    }

    private fun loadMovieDetail() {
        if (viewModel.movieDetail == null) {
            viewModel.getMovieDetail("${subjectBean?.id}")
                    .observe(this, Observer {
                        when (it.state) {
                            Resource.LOADING -> showLoading()
                            Resource.SUCCESS -> {
                                showContentView()
                                updateUi(it.data!!)
                                viewModel.movieDetail = it.data
                            }
                            Resource.ERROR -> {
                                showToast("${it.message}")
                                showError()
                            }
                        }
                    })
        } else {
            showContentView()
            updateUi(viewModel.movieDetail!!)
        }
    }

    //更新UI
    private fun updateUi(movieBean: MovieDetailBean) {
        //上映日期
        mHeaderView.tv_movie_day.text = String.format("上映日期：%s", movieBean.year)
        //制片国家
        mHeaderView.tv_movie_city.text = String.format("制片国家/地区：%s"
                , StringFormatUtil.formatGenres(movieBean.countries))
        //又名
        tv_movie_name_too.text = StringFormatUtil.formatGenres(movieBean.aka)
        //剧情简介
        tv_movie_introduce.text = movieBean.summary

        mMoreUrl = movieBean.alt
        mMovieName = movieBean.title

        transforData(movieBean)
    }

    /**
     * 异步线程转换数据
     */
    private fun transforData(movieBean: MovieDetailBean) {
        Thread {
            for (item in movieBean.directors) {
                item.type = "导演"
            }
            for (item in movieBean.casts) {
                item.type = "演员"
            }
            runOnUiThread {
                initPerson(movieBean)
            }
        }.start()
    }

    private fun initPerson(movieBean: MovieDetailBean) {
        //演员&导演
        adapter.clear()
        xrv_cast.visibility = View.VISIBLE
        adapter.addAll(movieBean.directors)
        adapter.addAll(movieBean.casts)
        adapter.notifyDataSetChanged()
    }

    override fun setHeaderImgUrl(): String {
        return "${subjectBean?.images?.medium}"
    }

    override fun setHeaderImageView(): ImageView {
        return mHeaderView.img_item_bg
    }

    override fun setHeaderLayout(): Int {
        return R.layout.header_slide_shape
    }

    //查看更多信息，豆瓣好像关闭了web上的所有链接
    override fun setTitleClickMore() {
        WebViewActivity.loadUrl(this@MovieDetailActivity, mMoreUrl, mMovieName)
    }

    override fun onRefresh() {
        loadMovieDetail()
    }

    companion object {
        fun start(context: Activity, subject: SubjectsBean, imageView: ImageView) {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra("bean", subject)
//            //共享元素动画
            val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context
                            , imageView
                            , CommonUtils.getString(R.string.transition_movie_img))
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }
}
