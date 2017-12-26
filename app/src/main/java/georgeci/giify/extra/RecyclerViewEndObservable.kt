package georgeci.giify.extra

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents

fun RecyclerView.end(limit: Int, emptyListCount: Int = 0) =
        this.scrollEvents()
                .map {
                    val recyclerView = it.view()
                    val position = recyclerView.getLastVisibleItemPosition()
                    val updatePosition = recyclerView.adapter.itemCount - 1 - limit / 2
                    position >= updatePosition
                }
                .filter { it }
                .map { Unit }


fun RecyclerView.getLastVisibleItemPosition(): Int {
    val manager = layoutManager
    return when(manager){
        is LinearLayoutManager -> manager.findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> manager.findLastVisibleItemPositions(null).max()!!
        else -> error("Unknown LayoutManager class: ${manager.javaClass}")
    }
}

fun RecyclerView.getFirstVisibleItemPosition(): Int {
    val manager = layoutManager
    return when(manager){
        is LinearLayoutManager -> manager.findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> manager.findFirstVisibleItemPositions(null).min()!!
        else -> error("Unknown LayoutManager class: ${manager.javaClass}")
    }
}