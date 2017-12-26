package georgeci.giify.screen.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import georgeci.giify.extra.SchedulerFactory
import georgeci.giify.extra.bind
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class ListBinding(
        private val view: ListView,
        private val router: ListRouter,
        private val viewModel: ListViewModel,
        private val schedulers: SchedulerFactory
) : LifecycleObserver {
    private val disposable = CompositeDisposable()
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        disposable.bind(schedulers) {
            viewModel.state.debounce(200, TimeUnit.MILLISECONDS, schedulers.computation()) toUi view::consumeState
            viewModel.routeCommand toUi { (position, item) -> router.route(position, item) }

            view.itemClickIntent fromUi viewModel.itemClickIntent
            view.loadNewPager fromUi viewModel.loadNewPager
            view.refreshIntent fromUi viewModel.refreshIntent
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbind() {
        disposable.clear()
    }
}