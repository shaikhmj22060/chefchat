// RecipeAdapter.kt
package com.example.chefchat.adapter  // or your preferred package

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chefchat.R
import com.example.chefchat.model.Recipe
import com.squareup.picasso.Picasso

class RecipeAdapter(private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivRecipeImage)
        val title: TextView = itemView.findViewById(R.id.tvRecipeTitle)
        val desc: TextView = itemView.findViewById(R.id.tvRecipeDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        holder.desc.text = recipe.description
        Picasso.get().load(recipe.imageUrl).into(holder.image)
    }

    override fun getItemCount(): Int = recipes.size
}
