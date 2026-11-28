package com.example.learnapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.learnapplication.databinding.FilmItemBinding

//вариант с отрисовкой в FilmViewHolder, получаем от RececlerAdapter только слушатель
class FilmViewHolder (itemView: View,
    private val clickListener: FilmListRecyclerAdapter.OnItemClickListener // Получаем слушатель
) : RecyclerView.ViewHolder(itemView) {
    private val filmItemBinding = FilmItemBinding.bind(itemView)
    //Привязываем view из layout к переменным
    private val title = filmItemBinding.title
    private val poster = filmItemBinding.poster
    private val description = filmItemBinding.description
    private val rvBtnFavorite = filmItemBinding.rvBtnFavorite
    private val rvBtnWatchLater = filmItemBinding.rvBtnWatchLater


    //В этом методе кладем данные из film в наши view
    fun bind(film: Film) {
        //Устанавливаем заголовок
        title.text = film.title
        //Устанавливаем постер
        poster.setImageResource(film.poster)
        //Устанавливаем описание
        description.text = film.description

        val favoriteIcon = if (film.favorite) R.drawable.ic_round_favorite else R.drawable.ic_round_unfavorite
        rvBtnFavorite.setImageResource(favoriteIcon)

        rvBtnFavorite.setOnClickListener {
            clickListener.onFavoriteClick(film, adapterPosition)
        }

        val watchLaterIcon = if (film.watchLater) R.drawable.ic_round_watch_later else R.drawable.ic_round_unpressed_watch_later
        rvBtnWatchLater.setImageResource(watchLaterIcon)

        rvBtnWatchLater.setOnClickListener {
            clickListener.onWatchLaterClick(film, adapterPosition)
        }

    }
}