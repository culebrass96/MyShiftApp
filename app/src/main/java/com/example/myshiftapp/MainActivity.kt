package com.example.myshiftapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Создаем простой текстовый интерфейс
        val textView = TextView(this).apply {
            text = "Мой график смен:\n\n" +
                   "Пн, 24 Ноя - День (08:00 - 20:00)\n" +
                   "Вт, 25 Ноя - Выходной\n" +
                   "Ср, 26 Ноя - Ночь (20:00 - 08:00)\n" +
                   "Чт, 27 Ноя - Отсыпной\n" +
                   "Пт, 28 Ноя - Выходной\n\n" +
                   "Здесь будут ваши смены!"
            textSize = 18f
            setPadding(50, 100, 50, 50)
        }
        
        setContentView(textView)
    }
}
