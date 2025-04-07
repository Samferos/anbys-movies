package iut.s4.sae.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import iut.s4.sae.R
import iut.s4.sae.model.Genres

class GenreAdapter(private val genres: Genres) : BaseAdapter() {
    override fun getCount() = genres.genres.size

    override fun getItem(position: Int) = genres.genres[position]

    override fun getItemId(position: Int) = genres.genres[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.genre_entry, parent, false)
        val title = view.findViewById<TextView>(R.id.genre_entry_title)
        title.text = genres.genres[position].name
        return view
    }
}