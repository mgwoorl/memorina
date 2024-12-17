package com.example.memorina_final

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var  firstCard: ImageView? = null
    private var guessedPairs = 0

    private var tags: Array<Int> = arrayOf()
    private val cardViews: ArrayList<ImageView> = arrayListOf()

    private val cardsImages = arrayOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
        R.drawable.card5, R.drawable.card6, R.drawable.card7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(i in 0..7){
            tags += i
            tags += i
        }
        tags.shuffle()

        for (i in 0..15) {
            val card = findViewById<ImageView>(resources.getIdentifier("card$i", "id", packageName))
            val cardTag = tags[i]
            card.tag = cardTag
            card.setOnClickListener(cardListener)
            cardViews.add(card)
        }

    }

    private val cardListener = View.OnClickListener() {
        lifecycleScope.launch (Dispatchers.Main)
        { setBackgroundWithDelay(it as ImageView) }
    }

    private suspend fun setBackgroundWithDelay(v: ImageView) {
        if (v == firstCard){
            return
        }
        val cardTag = v.tag.toString().filter { it.isDigit() }.toInt()
        v.setImageResource(cardsImages[cardTag])
        Log.e("Tag", "$cardTag")
        delay(500)

        if (firstCard == null) {
            firstCard = v
        }else{
            if (v.tag == firstCard!!.tag) {
                v.visibility = View.INVISIBLE; v.isClickable = false
                firstCard!!.visibility = View.INVISIBLE; firstCard!!.isClickable = false
                guessedPairs += 1
            } else {
                v.setImageResource(R.drawable.card_back); v.isClickable = true
                firstCard!!.setImageResource(R.drawable.card_back); firstCard!!.isClickable = true
                delay(500)
            }
            firstCard = null

            if (guessedPairs == 8) {
                Toast.makeText(this, "ВЫ ПОБЕДИЛИ!!!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}