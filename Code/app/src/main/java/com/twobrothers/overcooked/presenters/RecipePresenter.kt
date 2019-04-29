package com.twobrothers.overcooked.presenters

import android.os.Bundle
import com.twobrothers.overcooked.app.RecipeManager
import com.twobrothers.overcooked.interfaces.IRecipeContract
import com.twobrothers.overcooked.lookups.LookupIngredientType
import com.twobrothers.overcooked.models.recipe.RecipeModel
import com.twobrothers.overcooked.views.recipe.IngredientViewAdapter
import com.twobrothers.overcooked.views.recipe.MethodViewAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class RecipePresenter(private val view:IRecipeContract.View) : IRecipeContract.Presenter {

    private val disposable = CompositeDisposable()
    private var recipeModel: RecipeModel? = null

    override fun onStart(args: Bundle?) {
        RecipeManager
                .getRecipe(args!!.getString("id"))
                .subscribeBy(
                        onSuccess = {
                            recipeModel = it
                            view.render(it)
                            view.onMethodDataSetChanged()
                            view.onIngredientDataSetChanged()
                        },
                        onError =  { it.printStackTrace() }
                )
                .addTo(disposable)
    }

    override fun onStop() {
        disposable.dispose()
    }

    override fun onBindMethodRepositoryRowViewAtPosition(holder: MethodViewAdapter.Holder, position: Int) {
        val recipeModel = recipeModel
        recipeModel ?: return
        holder.render(position + 1, recipeModel.method[position])
    }

    override fun getMethodRepositoriesRowsCount(): Int {
        val recipeModel = recipeModel
        recipeModel ?: return 0
        return recipeModel.method.size
    }

    override fun onBindIngredientRepositoryRowViewAtPosition(holder: IngredientViewAdapter.Holder, position: Int) {
        val recipeModel = recipeModel
        recipeModel ?: return
        holder.render(recipeModel.ingredients[position], recipeModel.food)
    }

    override fun getIngredientRepositoriesRowsCount(): Int {
        val recipeModel = recipeModel
        recipeModel ?: return 0
        return recipeModel.ingredients.size
    }

    override fun getIngredientItemViewType(position: Int): Int {
        val recipeModel = recipeModel
        recipeModel ?: return -1

        val ingredient = recipeModel.ingredients[position]
        return LookupIngredientType.dataLookup(ingredient.ingredientType).id
    }

}
