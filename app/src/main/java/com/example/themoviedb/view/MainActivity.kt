package com.example.themoviedb.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.themoviedb.R
import com.example.themoviedb.model.MovieClasses.Movie
import com.example.themoviedb.model.MovieDBApiKey
import com.example.themoviedb.model.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, MovieAdapter.RvItemClickListener {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private  lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private  var adapter: MovieAdapter? = null
    private var sessionId = "1";
    private lateinit var movieList: MutableList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews(this)
        initAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            adapter?.clearAll()
            getMovies()
        }

        getMovies()
    }

    private fun initAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(itemClickListener = this)
        recyclerView.adapter = adapter
    }
    private fun bindViews(view: MainActivity) = with(view) {
        recyclerView = findViewById(R.id.recy_feed)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
    }

    private fun getMovies() {
        launch {
            swipeRefreshLayout.isRefreshing = true
            try {
                val response = ServiceBuilder.getPostApi().getPopularMoviesList(MovieDBApiKey)
                movieList = mutableListOf()
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        for (movie: Movie in movies.movieList) {
                            movieList.add(movie)
                        }
                    }
                    adapter?.movies = movieList
                    adapter?.notifyDataSetChanged()

                }
                swipeRefreshLayout.isRefreshing = false
            }
            catch (e: Exception) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun itemClick(position: Int, movie: Movie) {
        val intent = Intent(this, SingleMovieActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }

    override fun addToFavourites(position: Int, item: Movie) {
        return;
    }


}
