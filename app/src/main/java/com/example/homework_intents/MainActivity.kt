package com.example.homework_intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.homework_intents.databinding.ActivityMainBinding
import com.example.homework_intents.databinding.ActivityTimerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnSendTime()
        setOnShare()
    }

    private fun setOnShare() {
        binding.materialButton1.setOnClickListener {
            if (isValid()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, binding.textInput.text.toString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun setOnSendTime() {
        binding.materialButton.setOnClickListener {
            if (isValid()) {
                val intent = Intent(this, TimerActivity::class.java)
                intent.putExtra(ArgumentKey.NAME.name, binding.textInput.text.toString().toLong())
                startActivity(intent)
            }
        }
    }

    private fun isValid(): Boolean {
        val inputText = binding.textInput.text.toString()
        return inputText.isNotBlank() && inputText.toIntOrNull() != null
    }
}

enum class ArgumentKey {
    NAME
}