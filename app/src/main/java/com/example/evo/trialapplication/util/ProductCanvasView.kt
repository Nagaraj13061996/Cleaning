package com.example.evo.trialapplication.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductData

   class ProductCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {

     var productDataList: List<ProductData> = mutableListOf() // Initialize with your data

    private val textPaint = Paint().apply {
        textSize = resources.getDimensionPixelSize(R.dimen.text_size15sp).toFloat()
        color = Color.BLACK
    }
    fun setProductList(_productList: List<ProductData>) {
        productDataList=_productList
        invalidate() // Trigger redraw when data changes
    }
    private val rowHeight = resources.getDimensionPixelSize(R.dimen.dimen_30)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentY = rowHeight
        for (productData in productDataList) {
            drawRow(canvas, productData, currentY)
            currentY += rowHeight
        }
    }

    private fun drawRow(canvas: Canvas, productData: ProductData, y: Int) {
        var currentX = 0
        val columnWidth = width / 5 // Assuming there are 5 columns

        canvas.drawText(productData.product, currentX.toFloat(), y.toFloat(), textPaint)
        currentX += columnWidth

        canvas.drawText(productData.qty, currentX.toFloat(), y.toFloat(), textPaint)
        currentX += columnWidth
//
        canvas.drawText("100", currentX.toFloat(), y.toFloat(), textPaint)
        currentX += columnWidth

        canvas.drawText("10", currentX.toFloat(), y.toFloat(), textPaint)
        currentX += columnWidth

        canvas.drawText("17", currentX.toFloat(), y.toFloat(), textPaint)
    }
}
