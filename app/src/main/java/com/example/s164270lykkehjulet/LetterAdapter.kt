package com.example.s164270lykkehjulet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class LetterAdapter(val letterClickFunction: (String) -> Unit) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>(){

    private val list = ('A').rangeTo('Z').toList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LetterViewHolder {
        return LetterViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.item_view, p0, false)
        )
    }

    override fun onBindViewHolder(p0: LetterViewHolder, p1: Int) {
        val letter = list[p1]
        p0.button.text = letter.toString()
        p0.button.setOnClickListener { letterClickFunction(letter.toString()) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class LetterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.letter_button_item)
    }
}