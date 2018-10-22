package com.bigfriedicecream.overcooked.models

import com.bigfriedicecream.overcooked.lookups.LookupIngUnitType

class NormalRecipeIngredient:BaseRecipeIngredient() {
    var quantity:Int = 0
    var ingredientId:String = ""
    var name:String = ""
    var namePlural:String = ""
    var ingUnitTypeId:Int = LookupIngUnitType.Singular.id
}