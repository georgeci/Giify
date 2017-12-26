package georgeci.giify.screen.list

import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import georgeci.giify.R
import georgeci.giify.extra.StateWidget
import georgeci.giify.extra.end
import georgeci.giify.model.ImageEntity
import georgeci.giify.screen.list.adapter.ListAdapter
import io.reactivex.Observable

class ListViewImpl(
        view: View,
        private val adapter: ListAdapter,
        layoutManager: RecyclerView.LayoutManager
) : ListView {

    private val root: View = view.findViewById(R.id.list_root)
    private val stateView: StateWidget = view.findViewById(R.id.list_state)
    private val refresh: SwipeRefreshLayout = view.findViewById(R.id.list_content_state)
    private val list: RecyclerView = view.findViewById(R.id.list_content)
    private val errorButton: Button = view.findViewById(R.id.list_error_state_button)

    init {
        list.layoutManager = layoutManager
        list.adapter = adapter
    }

    override fun consumeState(state: ListState) {
        when (state) {
            is ListState.Content.Idle -> {
                refresh.isRefreshing = false
                stateView.show = R.id.list_content_state
                adapter.update(state.list.map { ListAdapter.ItemWrapper.Content(it) })
            }
            is ListState.Content.Error -> {
                refresh.isRefreshing = false
                stateView.show = R.id.list_content_state
                adapter.update(state.list.map { ListAdapter.ItemWrapper.Content(it) } + ListAdapter.ItemWrapper.Error)
            }
            is ListState.Content.ErrorToast -> {
                refresh.isRefreshing = false
                stateView.show = R.id.list_content_state
                adapter.update(state.list.map { ListAdapter.ItemWrapper.Content(it) })
                Snackbar.make(root, "Error", Snackbar.LENGTH_SHORT).show()
            }
            is ListState.Content.LoadMore -> {
                stateView.show = R.id.list_content_state
                adapter.update(state.list.map { ListAdapter.ItemWrapper.Content(it) } + ListAdapter.ItemWrapper.Progress)
            }
            is ListState.Content.Refresh -> {
                refresh.isRefreshing = true
                stateView.show = R.id.list_content_state
                adapter.update(state.list.map { ListAdapter.ItemWrapper.Content(it) })
            }
            ListState.FullScreenError -> stateView.show = R.id.list_error_state
            ListState.FullScreenProgress -> stateView.show = R.id.list_progress_state
        }
    }

    override val refreshIntent: Observable<Unit> = Observable.merge(
            refresh.refreshes(),
            errorButton.clicks()
    )

    override val loadNewPager: Observable<Unit> = list.end(6).doOnNext { Log.i("qoq", "loadMore") }
    override val itemClickIntent: Observable<Pair<Int, ImageEntity>> = adapter.itemClick
}
