package com.example.stockexchangeapp


class MyStockList{
  companion object {
    val stockList = ArrayList<StockModel>()
    init {
      stockList.add(StockModel("Apple",0.0,0.0))
      stockList.add(StockModel("Amazon",0.0,0.0))
    }
  }
}