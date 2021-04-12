package com.pozut.dortislem

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.pozut.dortislem.databinding.ActivityMainBinding
import java.lang.reflect.Array.getInt
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    var answer : Int = 0
    var question_count : Int = 0
    var correct_count : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val high_score = "hs" // for preferences to hol high score in this value




        val timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTw.text  = ""+millisUntilFinished/1000
            }

            override fun onFinish() {


                // Get high score with preferences and check if it is bigger than correct_count
                val preferences = getSharedPreferences("Score", Context.MODE_PRIVATE)
                var highscr = preferences.getInt(high_score,0)

                if (correct_count > highscr){
                    val editor = preferences.edit()
                    editor.putInt(high_score, correct_count).apply()
                    highscr = preferences.getInt(high_score,0)
                }



                // Alert dialog, two options evet or hayır
                // evet = play again, hayır = exit the app
                val builder:AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("En Yüksek Skor: " + highscr + " Skor: "+ correct_count)
                builder.setMessage("Yeniden oyna?")


                builder.setPositiveButton("Evet") { dialog, which ->
                    this.start()
                    // Setting the text views for start of the game
                    binding.feedBacktw.text = ""
                    question_count = 0
                    correct_count = 0
                    binding.counterTw.text = "" + correct_count + "/" + question_count
                    generateQuestion()
                }

                builder.setNegativeButton("Hayır") { dialog, which ->
                    finishAffinity()
                }


                val alertDialog:AlertDialog = builder.create()
                alertDialog.show()
            }
        }


        // Set all the buttons click listeners to 'this' if we want to use onClick override fun.
        binding.answ1Btn.setOnClickListener(this)
        binding.answ2Btn.setOnClickListener(this)
        binding.answ3Btn.setOnClickListener(this)
        binding.answ4Btn.setOnClickListener(this)

        binding.StartBtn.setOnClickListener {
            timer.start()
            binding.StartBtn.visibility = View.INVISIBLE
            binding.answ1Btn.visibility = View.VISIBLE
            binding.answ2Btn.visibility = View.VISIBLE
            binding.answ3Btn.visibility = View.VISIBLE
            binding.answ4Btn.visibility = View.VISIBLE
            generateQuestion()

        }

        binding.counterTw.text = "" + correct_count + "/" + question_count
    }


    // Get which button pressed instead of creating a listener and functions for each button
    override fun onClick(v: View?) {
        val buton = v as Button
        if (buton.text.toString() == answer.toString()){
            correct_count++
            question_count++
            binding.counterTw.text = "" + correct_count + "/" + question_count
            binding.feedBacktw.text = "Doğru"
            generateQuestion()
        }
        else{
            question_count++
            binding.counterTw.text = "" + correct_count + "/" + question_count
            binding.feedBacktw.text = "Yanlış"
            generateQuestion()
        }
    }



     fun generateQuestion(){
        val operation = (0..3).random()
        val number1 = (1..25).random()
        val number2 = (1..25).random()


        when (operation){   // generating question and writing it on textview
            0 ->{
                answer = number1 + number2
                binding.questionTw.text = "" + number1 + "+" + number2
            }
            1 -> {
                answer = number1 - number2
                binding.questionTw.text = "" + number1 + "-" + number2
            }
            2 ->{
                answer = number1 * number2
                binding.questionTw.text = "" + number1 + "x" + number2
            }
            3 ->{
                answer = generateDivision()
            }
        }
        // Shuffle numbers between answer and take 3 of them
        // Shuffles make sures that that 3 number we picked is different from each other
        var random_list = (answer - 10 .. answer + 10).shuffled().take(3)
        while (true){
            if (random_list[0] != answer && random_list[1] != answer && random_list[2] != answer){
                 break
            }
            random_list = (answer - 10 .. answer + 10).shuffled().take(3)
        }

         // Setting the correct answer's button randomly
        val correct_button = (0..3).random()
        when (correct_button){      // Setting the buttons values randomly
            0 ->{
                binding.answ1Btn.text = "" + answer
                binding.answ2Btn.text = "" + random_list[0]
                binding.answ3Btn.text = "" + random_list[1]
                binding.answ4Btn.text = "" + random_list[2]

            }
            1 ->{
                binding.answ1Btn.text = "" + random_list[0]
                binding.answ2Btn.text = "" + answer
                binding.answ3Btn.text = "" + random_list[1]
                binding.answ4Btn.text = "" + random_list[2]
            }
            2 ->{
                binding.answ1Btn.text = "" + random_list[0]
                binding.answ2Btn.text = "" + random_list[1]
                binding.answ3Btn.text = "" + answer
                binding.answ4Btn.text = "" + random_list[2]
            }
            3 ->{
                binding.answ1Btn.text = "" + random_list[0]
                binding.answ2Btn.text = "" + random_list[1]
                binding.answ3Btn.text = "" + random_list[2]
                binding.answ4Btn.text = "" + answer
            }
        }


    }

    // If we try to divide two random numbers the result wont be integer every time
    // this func. make sures that our division are integer
    fun generateDivision():Int{

        var number1 = (1..25).random()
        var number2 = (1..10).random()
        while (true){
            if (number1 % number2 == 0)
                break
            number1 = (1..25).random()
            number2 = (1..10).random()
        }

        binding.questionTw.text = "" + number1 + "/" + number2
        return number1/number2
    }



}
