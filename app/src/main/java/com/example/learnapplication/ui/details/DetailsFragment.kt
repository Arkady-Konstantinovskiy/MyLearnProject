package com.example.learnapplication.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.learnapplication.Film
import com.example.learnapplication.FilmListRecyclerAdapter
import com.example.learnapplication.R
import com.example.learnapplication.databinding.FragmentDetailsBinding
import com.example.learnapplication.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Вспомогательный метод для смены иконки
    private fun updateFavoriteIcon() {
        if (film.isInFavorites) {
            // Для FloatingActionButton используем setIconResource
            binding.detailsFabFavorite.setIconResource(R.drawable.ic_round_favorite)
        } else {
            binding.detailsFabFavorite.setIconResource(R.drawable.ic_round_unfavorite)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilmsDetails()

        binding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Укзываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //УКазываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        binding.detailsFabFavorite.setOnClickListener {
            // 1. Вызываем метод из адаптера, чтобы изменить статус в filmsDataBase
            // Мы используем имя класса, так как метод находится в companion object
            FilmListRecyclerAdapter.toggleFavoriteInDatabase(film)

            // 2. Обновляем состояние локального объекта film.
            // Это важно, потому что film в этом фрагменте — это копия,
            // и нам нужно, чтобы ее поле isInFavorites изменилось.
            film.isInFavorites = !film.isInFavorites

            // 3. Обновляем иконку на самой кнопке FAB
            updateFavoriteIcon()

            // 3. Выводим уведомление пользователю в зависимости от нового состояния
            val message = if (film.isInFavorites) "Добавлено в избранное" else "Удалено из избранного"
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }

        binding.detailsFabWatchLater.setOnClickListener {
            Snackbar.make(binding.root, "Смотреть позже", Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun setFilmsDetails() {
        //Получаем наш фильм из переданного бандла
        film = arguments?.get("film") as Film

        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title
        //Устанавливаем картинку
        binding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
        // ВАЖНО: Устанавливаем начальное состояние иконки при открытии
        updateFavoriteIcon()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}