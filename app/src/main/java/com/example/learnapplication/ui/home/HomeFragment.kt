package com.example.learnapplication.ui.home

import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.example.learnapplication.ui.details.DetailsFragment
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



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

//                    override fun onFavoriteClick(
//                        film: Film,
//                        position: Int
//                    ) {
//                        // 1. Меняем данные в "базе" через companion object
//                        FilmListRecyclerAdapter.toggleFavoriteInDatabase(film)
//
//                        // 2. Просим адаптер перерисовать весь список (в Home все фильмы на месте)
//                        filmsAdapter.updateList(FilmListRecyclerAdapter.filmsDataBase)
//                    }
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

