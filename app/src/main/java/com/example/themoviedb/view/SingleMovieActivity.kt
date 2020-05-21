package com.example.themoviedb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.themoviedb.R
import com.example.themoviedb.model.MovieDBApiKey
import com.example.themoviedb.model.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);
        val movieID = intent.getIntExtra("movie_id", 1);

        bindViews()
        getMovie(movieID)

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
        backBtn.setOnClickListener{
            finish();
        }
    }

    private fun getMovie(id: Int) {
        launch {
            try {
                val response = ServiceBuilder.getPostApi().getMovie(id, MovieDBApiKey)
                if (response.isSuccessful) {
                    val singleMovie = response.body()
                    if (singleMovie != null) {
                        mainLayout.visibility = View.VISIBLE
                        title.text = singleMovie.title
                        releaseDate.text = singleMovie.releaseDate
                        plot.text = singleMovie.overview
                        rating.text = singleMovie.voteAverage.toString()
                        tvToolbarTitle.text = singleMovie.title
                        Picasso.get()
                            .load("https://image.tmdb.org/t/p/w500" + singleMovie.posterPath)
                            .into(poster)
                    }
                }
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
            }
        }
    }

}
