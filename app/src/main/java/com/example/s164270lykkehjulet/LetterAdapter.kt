package com.example.s164270lykkehjulet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class LetterAdapter : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>(){

    private val list = ('A').rangeTo('Z').toList()

    class LetterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.letter_button_item)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LetterViewHolder {
        return LetterViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.item_view, p0, false)
        )
    }

    override fun onBindViewHolder(p0: LetterViewHolder, p1: Int) {
        val letter = list.get(p1)
        p0.button.text = letter.toString()

        p0.button.setOnClickListener {
            Toast.makeText(p0.view.context, "AAAA", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}