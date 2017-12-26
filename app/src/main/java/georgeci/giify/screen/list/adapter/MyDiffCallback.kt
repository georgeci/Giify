package georgeci.giify.screen.list.adapter

import android.support.v7.util.DiffUtil


class MyDiffCallback(
        private var newPersons: List<ListAdapter.ItemWrapper>,
        private var oldPersons: List<ListAdapter.ItemWrapper>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldPersons.size
    }

    override fun getNewListSize(): Int {
        return newPersons.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldPersons[oldItemPosition]
        val newItem = newPersons[newItemPosition]
        return when{
            oldItem is ListAdapter.ItemWrapper.Error && newItem is ListAdapter.ItemWrapper.Error -> true
            oldItem is ListAdapter.ItemWrapper.Progress && newItem is ListAdapter.ItemWrapper.Progress -> true
            oldItem is ListAdapter.ItemWrapper.Content && newItem is ListAdapter.ItemWrapper.Content ->
                    oldItem.item.path == newItem.item.path
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldPersons[oldItemPosition]
        val newItem = newPersons[newItemPosition]
        return when{
            oldItem is ListAdapter.ItemWrapper.Error && newItem is ListAdapter.ItemWrapper.Error -> true
            oldItem is ListAdapter.ItemWrapper.Progress && newItem is ListAdapter.ItemWrapper.Progress -> true
            oldItem is ListAdapter.ItemWrapper.Content && newItem is ListAdapter.ItemWrapper.Content ->
                oldItem.item.path == newItem.item.path
            else -> false
        }
    }
}