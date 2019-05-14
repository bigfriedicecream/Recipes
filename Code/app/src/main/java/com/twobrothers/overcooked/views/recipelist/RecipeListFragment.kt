package com.twobrothers.overcooked.views.recipelist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twobrothers.overcooked.R
import com.twobrothers.overcooked.interfaces.IRecipeListContract
import com.twobrothers.overcooked.presenters.RecipeListPresenter
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment : Fragment(), IRecipeListContract.View {

    private val presenter: IRecipeListContract.Presenter = RecipeListPresenter(this)

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        viewManager = LinearLayoutManager(context)
        viewAdapter = RecipeListViewAdapter(presenter)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_container).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun render() {
        progress_loading.visibility = View.GONE
        recycler_container.visibility = View.VISIBLE
        recycler_container.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleChildCount = viewManager.childCount
                val numChildren = viewManager.itemCount
                val visibleChildIndex = (viewManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (visibleChildIndex + visibleChildCount >= numChildren) {
                    presenter.loadRecipes()
                }
            }
        })
    }

    override fun onDataSetChanged(positionStart: Int, itemCount: Int) {
        recyclerView.post {
            run {
                viewAdapter.notifyItemRangeChanged(positionStart, itemCount)
            }
        }
    }
}
