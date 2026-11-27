package com.example.learnapplication.ui.home

import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.learnapplication.Film
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnapplication.FilmListRecyclerAdapter
import com.example.learnapplication.MainActivity
import com.example.learnapplication.R
import com.example.learnapplication.TopSpacingItemDecoration
import com.example.learnapplication.databinding.FragmentHomeBinding
import androidx.core.widget.doOnTextChanged
import androidx.navigation.ui.setupWithNavController
import com.example.learnapplication.ui.details.DetailsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyckler()
        setupSearch() // Вызываем метод настройки поиска
        initMenuBtn()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun setupSearch() {
//        // 1. Находим нашу поисковую строку
//        val searchEditText = binding.searchTextEditText
//
//        // 2. Устанавливаем слушатель изменений текста
//        searchEditText.doOnTextChanged { text, _, _, _ ->
//            val query = text.toString().trim() // Получаем текст и удаляем пробелы по краям
//
//            // 3. Фильтруем список
//            val filteredList = if (query.isEmpty()) {
//                // Если строка поиска пустая — показываем весь список
//                FilmListRecyclerAdapter.filmsDataBase
//            } else {
//                // Если есть текст — фильтруем по названию (регистронезависимо)
//                FilmListRecyclerAdapter.filmsDataBase.filter { film ->
//                    film.title.contains(query, ignoreCase = true)
//                }
//            }
//
//            // 4. Обновляем адаптер отфильтрованным списком
//            // Благодаря вашему DiffUtil, это будет выглядеть плавно
//            filmsAdapter.updateList(filteredList)
//        }
//    }
    private fun setupSearch() {
        // 1. Находим нашу поисковую строку
        val searchEditText = binding.searchEditText

        // Вместо doOnTextChanged мы используем классический addTextChangedListener
        searchEditText.addTextChangedListener(object : android.text.TextWatcher {

            // 1. Вызывается ПЕРЕД тем, как текст изменился
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Здесь нам ничего не нужно делать, оставляем пустым
            }

            // 2. Вызывается в момент ИЗМЕНЕНИЯ текста (самый важный метод для поиска)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                // Создаем новый список для результатов поиска
                val filteredList = mutableListOf<Film>()

                if (query.isEmpty()) {
                    // Если поиск пустой, копируем весь список из базы
                    filteredList.addAll(FilmListRecyclerAdapter.filmsDataBase)
                } else {
                    // Можно использовать .filter { ... }
                    // или обычный цикл for
                    for (film in FilmListRecyclerAdapter.filmsDataBase) {
                        // Если название фильма содержит текст запроса (без учета регистра)
                        if (film.title.contains(query, ignoreCase = true)) {
                            filteredList.add(film) // Добавляем подходящий фильм в список
                        }
                    }
                }

                // Отправляем полученный список в адаптер
                filmsAdapter.updateList(filteredList)
            }

            // 3. Вызывается ПОСЛЕ того, как текст изменился
            override fun afterTextChanged(p0: Editable) {
                // Здесь нам ничего не нужно делать
            }
        })
    }

    private fun initMenuBtn() {
        binding.toolbarHome.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Toast.makeText(requireContext(), "Ночная тема выкл", Toast.LENGTH_SHORT).show()
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        Toast.makeText(requireContext(), "Ночная тема вкл", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyckler() {
        binding.mainRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
//                        Вариант с использованием Navigate
                        val bundle = Bundle()
                        bundle.putParcelable("film", film)
                        (activity as MainActivity).findNavController(R.id.nav_host_fragment_content_main).navigate(
                            R.id.action_nav_selections_to_nav_details, bundle)

                    }

                    override fun onFavoriteClick(
                        film: Film,
                        position: Int
                    ) {
                        // 1. Меняем данные в "базе" через companion object
                        FilmListRecyclerAdapter.toggleFavoriteInDatabase(film)

                        // 2. Просим адаптер перерисовать весь список (в Home все фильмы на месте)
                        filmsAdapter.updateList(FilmListRecyclerAdapter.filmsDataBase)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(12)
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.updateList(FilmListRecyclerAdapter.filmsDataBase)
    }
}

