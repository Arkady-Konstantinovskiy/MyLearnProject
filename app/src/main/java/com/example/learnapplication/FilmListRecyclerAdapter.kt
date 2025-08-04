package com.example.learnapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.film_item.view.*

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса активити
class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Здесь у нас хранится список элементов для RV
    private var items = mutableListOf<Film>()
    private val filmsDataBase = mutableListOf(
        Film("The Godfather", R.drawable.p_5, "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son."),
        Film("The Dark Knight", R.drawable.p_6, "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice."),
        Film("Pulp Fiction", R.drawable.p_7, "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption."),
        Film("Inception", R.drawable.p_8, "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O."),
        Film("Hamilton", R.drawable.p_9, "The real life of one of America's foremost founding fathers and first Secretary of the Treasury, Alexander Hamilton. Captured live on Broadway from the Richard Rodgers Theater with the original Broadway cast."),
        Film("Gisaengchung", R.drawable.p_10, "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan."),
        Film("Interstellar", R.drawable.p_11, "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.")
    )


    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш view holder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))
    }

    //В этом методе будет привзяка полей из объекта Film, к view из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        when (holder) {
            is FilmViewHolder -> {
                //Вызываем метод bind(), который мы создали и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //напрмер, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.itemView.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

//    private val diffUtilCallback : DiffUtil.ItemCallback<Film> = object : DiffUtil.ItemCallback<Film>(){
//        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
//            TODO("Not yet implemented")
//        }
//
//    }

    fun addItems() {
//        val filmListDiff = FilmListDiff(items, newList)
//        val diffResult = DiffUtil.calculateDiff(filmListDiff)
        items = filmsDataBase


    }
//
//    private val asyncListDiffer = AsyncListDiffer(this,  )

    //Метод для добавления объектов в наш список
//    fun addItems(list: MutableList<Film> = filmsDataBase) {
//        //Сначала очишаем(если не реализовать DiffUtils)
//        items.clear()
//        //Добавляем
//        items.addAll(list)
//        //Уведомляем RV, что пришел новый список и ему нужно заново все "привязывать"
//        notifyDataSetChanged()
//    }



    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }
}