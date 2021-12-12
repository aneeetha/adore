package com.example.adore.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NoFilterArrayAdapter<String>(context: Context, layout: Int, var values: MutableList<String>) :
        ArrayAdapter<String>(context, layout, values) {
        private val filterThatDoesNothing = object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.values = values
                results.count = values.size
                return results
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }

        override fun getFilter(): Filter = filterThatDoesNothing
    }
