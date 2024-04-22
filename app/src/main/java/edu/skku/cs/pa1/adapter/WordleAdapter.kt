package edu.skku.cs.pa1.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.pa1.R
import edu.skku.cs.pa1.model.WordleItem
import edu.skku.cs.pa1.databinding.ItemWordleBinding

class WordleAdapter : RecyclerView.Adapter<WordleAdapter.ViewHolder>() {

    private val list: ArrayList<WordleItem> = arrayListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemWordleBinding.bind(view)
        val adapter = CharAdapter()
        private val marginBetween = itemView.context.resources.getDimensionPixelSize(R.dimen.margin_small)

        init {
            binding.wordRecycler.adapter = adapter
            binding.wordRecycler.addItemDecoration(MarginBetweenItemDecorator(marginBetween))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapter.updateList(list[position].chars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wordle, parent, false))


    override fun getItemCount(): Int = list.size

    fun updateList(list: List<WordleItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}