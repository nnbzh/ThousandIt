package com.example.themoviedb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.themoviedb.R
import com.example.themoviedb.model.MovieClasses.Movie
import com.example.themoviedb.view_model.MoviesListViewModel
import com.example.themoviedb.view_model.ViewModelProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, MovieAdapter.RvItemClickListener {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private  lateinit var recyclerView: RecyclerView
    private  var adapter: MovieAdapter? = null
    private lateinit var movieList: MutableList<Movie>
    private lateinit var moviesListViewModel: MoviesListViewModel
    private lateinit var layoutManager: LinearLayoutManager

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        bindViews()
        initAdapter()
        getMovies()

    }

    private fun initViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        moviesListViewModel = ViewModelProvider(this, viewModelProviderFactory).
            get(MoviesListViewModel::class.java)
    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.recy_feed)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            adapter?.clearAll()
            getMovies()
        }
    }

    private fun initAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(itemClickListener = this)
        recyclerView.adapter = adapter
    }

    private fun getMovies() {
        moviesListViewModel.getMovies()
        moviesListViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is MoviesListViewModel.State.ShowLoading -> {
                    swipeRefreshLayout.isRefreshing = true
                }
                is MoviesListViewModel.State.HideLoading -> {
                    swipeRefreshLayout.isRefreshing = false
                }
                is MoviesListViewModel.State.Update -> {

                }
                is MoviesListViewModel.State.Result -> {
                    adapter?.movies = result.moviesList
                    adapter?.notifyDataSetChanged()
                }
            }
        })
    }


    override fun itemClick(position: Int, movie: Movie) {
        val intent = Intent(this, SingleMovieActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }

    override fun addToFavourites(position: Int, item: Movie) {
        moviesListViewModel.addToFavourites(item)
    }

//    private fun onPopularMoviesFetched(movies: List<Movie>) {
//        adapter?.appendMovies(movies)
//        attachOnScrollListener()
//    }
//
//    private fun attachOnScrollListener() {
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val totalItemCount = layoutManager.itemCount
//                val visibleItemCount = layoutManager.childCount
//                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
//
//                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
//                    recyclerView.removeOnScrollListener(this)
//                    page++
//                    getMovies()
//                }
//            }
//        })
//    }

}
