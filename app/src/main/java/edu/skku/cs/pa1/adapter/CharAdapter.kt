package edu.skku.cs.pa1.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.pa1.model.CharUiModel
import edu.skku.cs.pa1.R
import edu.skku.cs.pa1.databinding.ItemCharBinding

class CharAdapter : RecyclerView.Adapter<CharAdapter.ViewHolder>() {

    private val list: ArrayList<CharUiModel> = arrayListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCharBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(list[position]) {
        holder.binding.charText.text = this.char
        holder.binding.charText.setTextColor(ContextCompat.getColor(holder.itemView.context, this.state.textColorRes))
        holder.binding.charText.setBackgroundResource(this.state.colorRes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_char, parent, false))


    override fun getItemCount(): Int = list.size

    fun updateList(list: List<CharUiModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}