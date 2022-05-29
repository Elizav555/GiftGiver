package com.example.giftgiver.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.R


abstract class MySwipeCallback(private val context: Context) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlag)
    }

    override fun isLongPressDragEnabled(): Boolean = true

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight: Int = itemView.height
        val isCancelled = dX == 0f && !isCurrentlyActive
        if (isCancelled) {
            clearCanvas(
                canvas,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            return
        }
        val background = ColorDrawable()
        background.color = ContextCompat.getColor(context, android.R.color.holo_red_light)
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)
        val deleteDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete)
        deleteDrawable?.let {
            val intrinsicWidth = deleteDrawable.intrinsicWidth;
            val intrinsicHeight = deleteDrawable.intrinsicHeight;
            val deleteIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft: Int = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight: Int = itemView.right - deleteIconMargin
            val deleteIconBottom: Int = deleteIconTop + intrinsicHeight
            deleteDrawable.setBounds(
                deleteIconLeft,
                deleteIconTop,
                deleteIconRight,
                deleteIconBottom
            )
            deleteDrawable.draw(canvas)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        val clearPaint = Paint()
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        c.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }
}
