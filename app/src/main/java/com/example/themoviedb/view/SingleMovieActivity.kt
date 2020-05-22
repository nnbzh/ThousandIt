package com.example.themoviedb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedb.R
import com.example.themoviedb.model.MovieClasses.Movie
import com.example.themoviedb.view_model.SingleMovieViewModel
import com.example.themoviedb.view_model.ViewModelProviderFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SingleMovieActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private lateinit var progressBar: ProgressBar
    private lateinit var backBtn: TextView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var plot: TextView
    private lateinit var rating: TextView
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var tvToolbarTitle: TextView
    private lateinit var duration: TextView
    private lateinit var singleMovieViewModel: SingleMovieViewModel
    private lateinit var budget: TextView
    private lateinit var revenue: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);
        initViewModel()
        bindViews()

        val movieID = intent.getIntExtra("movie_id", 1);
        getMovie(movieID)
    }

    private fun initViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        singleMovieViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SingleMovieViewModel::class.java)
    }

    private fun bindViews() {
        mainLayout = findViewById(R.id.mainLayout)
        mainLayout.visibility = View.INVISIBLE;

        progressBar = findViewById(R.id.progressBar)
        backBtn = findViewById(R.id.tvBack)
        poster = findViewById(R.id.ivBanner)
        title = findViewById(R.id.tvTitle)
        releaseDate = findViewById(R.id.tvReleaseDate)
        plot = findViewById(R.id.tvPlot)
        rating = findViewById(R.id.tvRating)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitile)
        duration = findViewById(R.id.tvDuration)
        budget = findViewById(R.id.tvBudget)
        revenue = findViewById(R.id.tvRevenue)
//        budget = findViewById(R.id.tvBudget)
//        revenue = findViewById(R.id.tvRevenue)
//        genres = findViewById(R.id.tvGenre)
//        producers = findViewById(R.id.tvProducers)

        backBtn.setOnClickListener{
            finish();
        }
    }

    private fun fillInfo(singleMovie: Movie) {
        mainLayout.visibility = View.VISIBLE
        title.text = singleMovie.title
        releaseDate.text = singleMovie.releaseDate
        plot.text = singleMovie.overview
        rating.text = singleMovie.voteAverage.toString()
        duration.text = "${getString(R.string.duration)} ${singleMovie.runtime.toString()} ${getString(R.string.min)}"
        budget.text = "${getString(R.string.budget)} ${singleMovie.budget.toString()} ${getString(R.string.dollar)}"
        revenue.text = "${getString(R.string.revenue)} ${singleMovie.revenue.toString()} ${getString(R.string.dollar)}"
//        budget.text = "${singleMovie.budget.toString()} ${getString(R.string.dollar)}"
//        revenue.text = "${singleMovie.revenue.toString()} ${getString(R.string.dollar)}"
//        genres.text = genresToString(singleMovie.genres)
//        producers.text = setProdNames(singleMovie.producers)
        tvToolbarTitle.text = singleMovie.title

        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + singleMovie.posterPath)
            .into(poster)
    }

    private fun getMovie(id: Int) {
        singleMovieViewModel.getMovie(id)
        singleMovieViewModel.liveData.observe(this, Observer { result ->
            when(result) {
                is SingleMovieViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is SingleMovieViewModel.State.Result -> {
                    mainLayout.visibility = View.VISIBLE
                    if (result.movie != null) {
                        fillInfo(result.movie)
                    }
                }
            }
        })
    }

//    private fun genresToString(genres: List<Genre>?): String {
//        var names = ""
//            for (genre in genres!!) names+= "${genre.name}, "
//        return names;
//    }
//
//    private fun setProdNames(producers: List<Producers>?) : String {
//        var producersName = ""
//            for (prod in producers!!) producersName+= "${prod.name}, "
//        return producersName;
//    }

}
