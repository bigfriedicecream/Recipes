package com.twobrothers.overcooked.models.recipe

class RecipeModel {
    val id: String = ""
    val title: String = ""
    val imageUrl: String = ""
    val prepTime: Int = 0
    val cookTime: Int = 0
    val method: ArrayList<String> = ArrayList()
    val ingredientSections: ArrayList<IngredientSection> = ArrayList()

    class IngredientSection {
        val heading: String = ""
        val ingredients: ArrayList<Any> = ArrayList()
    }

    class Ingredient {
        val ingredientType: Int? = null
    }

    class Quantified {
        val amount: Int = 0
        val unitIds: ArrayList<Int> = ArrayList()
        val foodId: String = ""
    }

    class FreeText {
        val description: String = ""
    }

    class Heading(val title: String = "")
}