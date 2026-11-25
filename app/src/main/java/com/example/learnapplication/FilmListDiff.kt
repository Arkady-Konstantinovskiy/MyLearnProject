package com.example.learnapplication

import androidx.recyclerview.widget.DiffUtil

class FilmListDiff(var oldList: MutableList<Film>, var newList: MutableList<Film>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFilm = oldList[oldItemPosition]
        val newFilm = newList[newItemPosition]
         return oldFilm.isInFavorites == newFilm.isInFavorites &&
                oldFilm.watchLater == newFilm.watchLater
    }

}