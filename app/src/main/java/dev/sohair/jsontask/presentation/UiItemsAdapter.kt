package dev.sohair.jsontask.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sohair.jsontask.data.ItemData
import dev.sohair.jsontask.databinding.ItemRecyvlerViewBinding

class UiItemsAdapter(
    private val items: List<ItemData>
) : RecyclerView.Adapter<UiItemsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemRecyvlerViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRecyvlerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvUserId.text = item.id.toString()
            tvTitle.text = item.title
            tvBody.text = item.body
            tvLatitude.text = item.latitude
            tvLongitude.text = item.longitude
            tvCompanyName.text = item.companyName
        }
    }
}