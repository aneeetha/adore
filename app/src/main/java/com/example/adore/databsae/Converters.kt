package com.example.adore.databsae

import androidx.room.TypeConverter
import com.example.adore.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListOfStringTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>{
        val listType = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)

}

class InStockCountTypeConverter{
    @TypeConverter
    fun fromList(list: List<Stock>): String{
        val listType = object : TypeToken<List<Stock>>(){}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    fun fromString(stockElements: String): List<Stock> = Gson().fromJson(stockElements, Array<Stock>::class.java).asList()
}


class CategoryTypeConverter{
    @TypeConverter
    fun fromString(categoryElements: String): Category = Gson().fromJson(categoryElements, Category::class.java)


    @TypeConverter
    fun fromCategory(category: Category): String{
        val type = object: TypeToken<Category>(){}.type
        return Gson().toJson(category, type)
    }
}

//        val category = categoryLabel.split(" > ")
//        val jsonStringCategory = JSONObject()
//        jsonStringCategory.put("genderCategory","${GenderCategory.valueOf(category[0])}")
//        jsonStringCategory.put("topLevelCategory","${TopLevelCategory.valueOf(category[1])}")
//        jsonStringCategory.put("subCategory","${SubCategory.valueOf(category[2])}")
//        Log.e("ConverterError", "$jsonObject")
//val jsonStringCategory = JSONObject("""{"genderCategory": ${GenderCategory.valueOf(category[0])},"topLevelCategory": ${TopLevelCategory.valueOf(category[1])}, "subCategory": ${SubCategory.valueOf(category[2])}}""")
//val jsonStringCategory = """{"genderCategory": ${category[0]},"topLevelCategory": ${category[1]}, "subCategory": ${category[2]}}"""
////      Log.e("ConverterError", "$jsonStringCategory")
