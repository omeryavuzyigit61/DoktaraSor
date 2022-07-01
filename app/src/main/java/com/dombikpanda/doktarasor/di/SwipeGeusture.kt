package com.dombikpanda.doktarasor.di

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dombikpanda.doktarasor.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGeusture(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    val deleteColor = ContextCompat.getColor(context, R.color.deletecolor)
    val archiveColor = ContextCompat.getColor(context, R.color.green)
    val deleteIcon = R.drawable.ic_delete_com
    val archiveIcon = R.drawable.ic_archive
    val deleteLabel = context.getString(R.string.delete)
    val deleteLabelColor = ContextCompat.getColor(context, R.color.white)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(archiveColor)
            .addSwipeRightActionIcon(archiveIcon)
            .addSwipeLeftLabel(deleteLabel)
            .setSwipeLeftLabelColor(deleteLabelColor)
            .setSwipeLeftLabelTextSize(0,50f)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}