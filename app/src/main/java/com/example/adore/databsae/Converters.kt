package com.example.adore.databsae

import androidx.room.TypeConverter
import com.example.adore.models.*
import com.example.adore.models.dataClasses.Stock
import com.example.adore.models.enums.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DateTypeConvertor{
    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(dateLong: Long?) = dateLong?.let { Date(dateLong) }
}

class ListOfStringTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String>{
        val listType = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)

}

class ListOfStockTypeConverter{
    @TypeConverter
    fun fromList(list: List<Stock>): String{
        val listType = object : TypeToken<List<Stock>>(){}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    fun fromString(stockElements: String): List<Stock> = Gson().fromJson(stockElements, Array<Stock>::class.java).asList()
}

class ProductCategoryTypeConverter{
    @TypeConverter
    fun fromList(list: List<ProductType>): String{
        val listType = object : TypeToken<List<ProductType>>(){}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    fun fromString(productTypes: String): List<ProductType> = Gson().fromJson(productTypes, Array<ProductType>::class.java).asList()
}

class CategoryTypeConverter{
    @TypeConverter
    fun fromList(list: List<Category>): String{
        val listType = object : TypeToken<List<Category>>(){}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    fun fromString(categories: String): List<Category> = Gson().fromJson(categories, Array<Category>::class.java).asList()
}

class ColorTypeConverter{
    @TypeConverter
    fun fromList(list: List<Color>): String{
        val listType = object : TypeToken<List<Color>>(){}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    fun fromString(colors: String): List<Color> = Gson().fromJson(colors, Array<Color>::class.java).asList()
}

class GenderTypeConverter{
    @TypeConverter
    fun fromGender(gender: Gender?): String?{
        val listType = object : TypeToken<Gender>(){}.type
        return gender?.let{Gson().toJson(gender, listType)}
    }

    @TypeConverter
    fun fromString(gender: String?): Gender? = gender?.let{Gson().fromJson(gender, Gender::class.java)}
}

class DistrictTypeConverter{
    @TypeConverter
    fun fromGender(district: District?): String?{
        val listType = object : TypeToken<District>(){}.type
        return district?.let{Gson().toJson(district, listType)}
    }

    @TypeConverter
    fun fromString(district: String?): District? = district?.let{Gson().fromJson(district, District::class.java)}
}

//class ListOfOrderProductsTypeConverter{
//    @TypeConverter
//    fun fromList(list: List<OrderProductDetails>): String{
//        val listType = object : TypeToken<List<OrderProductDetails>>(){}.type
//        return Gson().toJson(list, listType)
//    }
//
//    @TypeConverter
//    fun fromString(orderProductDetailsElement: String): List<OrderProductDetails> = Gson().fromJson(orderProductDetailsElement, Array<OrderProductDetails>::class.java).asList()
//}



//class CategoryTypeConverter{
//    @TypeConverter
//    fun fromString(categoryElements: String): Category = Gson().fromJson(categoryElements, Category::class.java)
//
//
//    @TypeConverter
//    fun fromCategory(category: Category): String{
//        val type = object: TypeToken<Category>(){}.type
//        return Gson().toJson(category, type)
//    }
//}

//        val category = categoryLabel.split(" > ")
//        val jsonStringCategory = JSONObject()
//        jsonStringCategory.put("genderCategory","${GenderCategory.valueOf(category[0])}")
//        jsonStringCategory.put("topLevelCategory","${TopLevelCategory.valueOf(category[1])}")
//        jsonStringCategory.put("subCategory","${SubCategory.valueOf(category[2])}")
//        Log.e("ConverterError", "$jsonObject")
//val jsonStringCategory = JSONObject("""{"genderCategory": ${GenderCategory.valueOf(category[0])},"topLevelCategory": ${TopLevelCategory.valueOf(category[1])}, "subCategory": ${SubCategory.valueOf(category[2])}}""")
//val jsonStringCategory = """{"genderCategory": ${category[0]},"topLevelCategory": ${category[1]}, "subCategory": ${category[2]}}"""
////      Log.e("ConverterError", "$jsonStringCategory")
