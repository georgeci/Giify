package georgeci.giify.screen.list.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Button
import georgeci.giify.R
import georgeci.giify.extra.AspectRatioImageView

sealed class ListViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ListItemViewHolder(view: View) : ListViewHolder(view) {
    val image: AspectRatioImageView = view.findViewById(R.id.image)
    val text: View = view.findViewById(R.id.image_position)
    var position: Int? = null
}

class ListProgressViewHolder(view: View) : ListViewHolder(view) {
    init {
        updateParamsForFullSpan(view)
    }
}

class ListErrorViewHolder(view: View) : ListViewHolder(view) {
    val loadMore = view.findViewById<Button>(R.id.load_more)
    init {
        updateParamsForFullSpan(view)
    }
}

private fun updateParamsForFullSpan(view: View) {
    val params = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
    params?.apply {
        params.isFullSpan = true
        view.layoutParams = params
    }
}