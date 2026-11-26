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

    companion object {
        val filmsDataBase = mutableListOf(
            Film(
                "The Godfather",
                R.drawable.p_5,
                "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son."
            ),
            Film(
                "The Dark Knight",
                R.drawable.p_6,
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice."
            ),
            Film(
                "Pulp Fiction",
                R.drawable.p_7,
                "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption."
            ),
            Film(
                "Inception",
                R.drawable.p_8,
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O."
            ),
            Film(
                "Hamilton",
                R.drawable.p_9,
                "The real life of one of America's foremost founding fathers and first Secretary of the Treasury, Alexander Hamilton. Captured live on Broadway from the Richard Rodgers Theater with the original Broadway cast."
            ),
            Film(
                "Gisaengchung",
                R.drawable.p_10,
                "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan."
            ),
            Film(
                "Interstellar",
                R.drawable.p_11,
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."
            )
        )

        fun toggleFavoriteInDatabase(film: Film) {
            // 1. Находим индекс фильма в общем списке
            val index =
                filmsDataBase.indexOfFirst { it.title == film.title } // Сравниваем по названию или ID
            if (index != -1) {
                // 2. ВАЖНО: Создаем КОПИЮ объекта с измененным значением
                // Если мы просто напишем film.isInFavorites = true, DiffUtil не сработает!
                val updatedFilm = filmsDataBase[index].copy(isInFavorites = !filmsDataBase[index].isInFavorites)

                // 3. Заменяем старый объект новым в списке
                filmsDataBase[index] = updatedFilm
            }
        }

    }


    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш view holder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        //вариант с отрисовкой в FilmViewHolder
                val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
                // Передаем clickListener прямо в ViewHolder при его создании
                return FilmViewHolder(view, clickListener)
        //        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))
    }

    //В этом методе будет привязка полей из объекта Film, к view из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        if (holder is FilmViewHolder) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                clickListener.click(items[position])
            }
        }
    }


    fun addItems(list: MutableList<Film> = filmsDataBase) {
        items.addAll(list)

    }
    fun updateList(newList: List<Film>) {
        // 1. Создаем "старый" список как копию текущего состояния,
        // чтобы изменения в items не повлияли на процесс сравнения.
        val oldList = items.toMutableList()

        // 2. Создаем новый список (тоже копию, чтобы избежать проблем с ссылками)
        val listToUpdate = newList.toMutableList()

        // 3. Инициализируем наш DiffCallback
        val diffCallback = FilmListDiff(oldList, listToUpdate)

        // 4. Вычисляем разницу между старым и новым списком
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // 5. Сообщаем адаптеру, какие именно элементы изменились, добавились или удалились.
        // 'this' — это текущий экземпляр адаптера.
        diffResult.dispatchUpdatesTo(this)

        // 6. Обновляем наш основной источник данных
        items.clear()
        items.addAll(listToUpdate)
    }

    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }
}