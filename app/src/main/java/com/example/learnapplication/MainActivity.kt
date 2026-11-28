package com.example.learnapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
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
                val currentDestination = navController.currentDestination
                val rootGraph = navController.graph
                val firstTabGraph = rootGraph.findNode(rootGraph.startDestinationId) as? NavGraph

                // Проверяем, находимся ли мы на стартовом экране первой вкладки
                val isAtRootStartDestination = currentDestination?.id == firstTabGraph?.startDestinationId

                // Проверяем, находимся ли мы на стартовом экране ЛЮБОЙ вкладки
                val isAtAnyTabRoot = currentDestination?.id == currentDestination?.parent?.startDestinationId

                when {
                    isAtRootStartDestination -> {
                        // Мы в начале первой вкладки — спрашиваем про выход
                        showExitDialog()
                    }
                    isAtAnyTabRoot -> {
                        // Мы в начале 2-й или 3-й вкладки — переключаемся на 1-ю вкладку
                        // так же, как это делает клик по BottomNavigationView (с restoreState = true)
                        binding.bottomNavigation.selectedItemId = rootGraph.startDestinationId

                    }
                    else -> {
                        // Мы внутри вкладки (например, на nav_details) — просто идем назад по стеку вкладки
                        navController.popBackStack()
                    }
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
