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
//        binding.toolbarMain.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.settings -> {
//                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                        Toast.makeText(this, "Ночная тема выкл", Toast.LENGTH_SHORT).show()
//                    } else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                        Toast.makeText(this, "Ночная тема вкл", Toast.LENGTH_SHORT).show()
//                    }
//                    true
//                }
//                else -> false
//            }
//        }

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
//    Красивый метод для кнопки "Назад",
//    но при возвращение на начальный экран пропускает экран details_fragment, если тот был ранее открыт с него.
//    Такова реализация кнопки "Назад" в navigation components по умолчанию.
//   private fun setupBackPressed() {
//    val exitCallback = object : OnBackPressedCallback(false) { // По умолчанию выключен!
//        override fun handleOnBackPressed() {
//            showExitDialog()
//        }
//    }
//    onBackPressedDispatcher.addCallback(this, exitCallback)
//
//    // Включаем наш перехватчик ТОЛЬКО когда пользователь находится на самом первом экране
//    navController.addOnDestinationChangedListener { controller, destination, _ ->
//        val rootGraph = controller.graph
//        val firstTabGraph = rootGraph.findNode(rootGraph.startDestinationId) as? NavGraph
//
//        // Callback активен только на стартовом экране первой вкладки
//        exitCallback.isEnabled = (destination.id == firstTabGraph?.startDestinationId)
//    }
//}


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
