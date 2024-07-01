package com.example.catchthekannykotlin

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.catchthekannykotlin.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

private lateinit var binding: ActivityMainBinding

lateinit var mediaPlayer : MediaPlayer
lateinit var backGround : MediaPlayer

private var score = 0
lateinit var countDownTimer: CountDownTimer
var time  = 20000L
private var difficulty = 1
private lateinit var runnable: Runnable
private val handler: Handler = Handler(Looper.getMainLooper())
private lateinit var imageViews: Array<ImageView>

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageViews = arrayOf(
            binding.imageView1,
            binding.imageView2,
            binding.imageView3,
            binding.imageView4,
            binding.imageView5,
            binding.imageView6,
            binding.imageView7,
            binding.imageView8,
            binding.imageView9
        )
         mediaPlayer = MediaPlayer.create(this,R.raw.ses)
        backGround = MediaPlayer.create(this,R.raw.muzik)

        startTimer(time)
        startGame()
    }

    private fun kennyLocation() {
        runnable = object : Runnable {
            override fun run() {
                hideImages()
                val i = Random.nextInt(imageViews.size)
                imageViews[i].visibility = View.VISIBLE
                handler.postDelayed(this, (1000 / difficulty).toLong())
            }
        }
    }

    private fun startGame() {
        kennyLocation()
        backGround.isLooping = true // Müziği döngüsel olarak çal
        backGround.start() // Müziği başlat
        handler.post(runnable)
    }

    fun kennyClicked(view: View) {
        score++
        mediaPlayer.start()
        increaseDifficulty()
        time += 5000 // Güncel süreye 5 saniye ekleyin
        countDownTimer.cancel() // Mevcut CountDownTimer'ı iptal edin
        startTimer(time) // Yeni süre ile CountDownTimer'ı başlatın
        binding.textScore.text = "Score : $score"
        view.visibility = View.INVISIBLE
    }



    private fun startTimer(timeInMillis : Long) {
       countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(p0: Long) {

                binding.textTime.text = "Time : ${p0 / 1000}"
            }

            override fun onFinish() {
               handler.removeCallbacks(runnable)
                backGround.stop()
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Tekrar Oyna")
                    .setMessage("Tekrar oynamak ister misin?")
                    .setPositiveButton("Yes") { _, _ ->
                        //Snackbar.make(binding.textTime, "Oyun başlıyor...", Snackbar.LENGTH_SHORT).show()
                        score = 0
                        binding.textScore.text = "Score: 0"
                        time = 20000L
                        difficulty =1
                        startTimer(time)
                        startGame()
                    }
                    .setNegativeButton("No") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()
            }
        }.start()
    }

    private fun hideImages() {
        for (imageView in imageViews) {
            imageView.visibility = View.INVISIBLE
        }
    }

    private fun increaseDifficulty(){

        difficulty++
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        backGround.release()
    }

}
