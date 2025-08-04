package com.example.learnapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.learnapplication.databinding.FilmItemBinding

//import kotlinx.android.synthetic.main.film_item.view.*

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        rvBtnFavorite.setOnClickListener(){
            if (!film.isInFavorites){
                rvBtnFavorite.setImageResource(R.drawable.ic_round_favorite)
                film.isInFavorites = true
            } else {
                rvBtnFavorite.setImageResource(R.drawable.ic_round_unfavorite)
                film.isInFavorites = false
            }
        }

        rvBtnWatchLater.setOnClickListener(){
            if (!film.watchLater){
                rvBtnWatchLater.setImageResource(R.drawable.ic_round_watch_later)
                film.watchLater = true
            } else {
                rvBtnWatchLater.setImageResource(R.drawable.ic_round_unpressed_watch_later)
                film.watchLater = false
            }
        }
//        rvBtnFavorite.setImageResource(
//            if (film.isInFavorites) R.drawable.ic_round_favorite
//            else R.drawable.ic_round_unfavorite
//        )
//        rvBtnWatchLater.setImageResource(
//            if(film.watchLater) R.drawable.ic_round_watch_later
//            else R.drawable.ic_round_unpressed_watch_later
//        )

    }
}