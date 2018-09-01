package com.example.stockexchangeapp


import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class StockListAdapter(private val stockList:ArrayList<StockModel>) : RecyclerView.Adapter<StockListAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.list_row, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stockList[position])

  override fun getItemCount(): Int = stockList.size

  fun addItem(item:StockModel){
    stockList.add(item)
    notifyDataSetChanged()
  }

  fun updateItem(item:StockModel) {
    stockList.forEachIndexed { index, element ->
      if (element.name == item.name){
        stockList[index].changeValue = item.changeValue
        stockList[index].currentValue = item.currentValue
        notifyItemChanged(index)
      }
    }
  }

  fun contains(item: StockModel):Boolean{
    for (stock in stockList){
      if (stock.name==item.name){
        return true
      }
    }
    return false
  }

  fun removeItem(name: String) {

    val it = stockList.iterator()
    while (it.hasNext()) {
      val value = it.next()
      if (value.name == name){
        it.remove()
      }

    }
    notifyDataSetChanged()

  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val changePercent: TextView = itemView.findViewById(R.id.changeValue)
    private val stockName: TextView = itemView.findViewById(R.id.stockName)
    private val currentValue: TextView = itemView.findViewById(R.id.currentValue)

    fun bind(item: StockModel) = with(itemView) {
      stockName.text = item.name
      currentValue.text = item.currentValue.toString()
      val fmt = "%s%s"

      changePercent.text = String.format(fmt, item.changeValue.toString(), "%")

      if (item.changeValue.toString().contains("-")){
        changePercent.background = ResourcesCompat.getDrawable(resources, android.R.color.holo_red_dark, null)

      } else {
        changePercent.background = ResourcesCompat.getDrawable(resources, android.R.color.holo_green_dark, null)
      }

    }
  }
}