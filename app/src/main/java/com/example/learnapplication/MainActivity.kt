package com.example.learnapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.learnapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 1. Объявляем navController как поле класса
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        setupBackPressed() // 2. Инициализируем обработку кнопки "Назад"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initNavigation() {
        binding.toolbarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Toast.makeText(this, "Ночная тема выкл", Toast.LENGTH_SHORT).show()
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        Toast.makeText(this, "Ночная тема вкл", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }

        val navView: BottomNavigationView = binding.bottomNavigation

        // Находим navController через NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }

    // 3. Метод для обработки нажатия кнопки "Назад"
    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Проверяем, является ли текущий фрагмент стартовым (Start Destination)
                // Это решение универсально: не нужно прописывать ID фрагмента вручную
                if (navController.currentDestination?.id == navController.graph.startDestinationId) {
                    showExitDialog()
                } else {
                    // Если мы не на главном экране, просто идем назад по стеку
                    navController.popBackStack()
                }
            }
        })
    }

    // 4. Метод для показа всплывающего меню
    private fun showExitDialog() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialogColorTheme)
            .setTitle("Выход из приложения")
            .setMessage("Вы действительно хотите покинуть приложение?")
            .setPositiveButton("Да") { _, _ ->
                finish() // Закрывает Activity
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}
