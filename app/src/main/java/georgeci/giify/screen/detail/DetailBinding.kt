package georgeci.giify.screen.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import georgeci.giify.extra.SchedulerFactory
import georgeci.giify.extra.bind
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class DetailBinding(
        private val view: DetailView,
        private val router: DetailRouter,
        private val viewModel: DetailViewModel,
        private val schedulers: SchedulerFactory
) : LifecycleObserver {
    private val disposable = CompositeDisposable()
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        disposable.bind(schedulers) {
            viewModel.state.debounce(200, TimeUnit.MILLISECONDS, schedulers.computation()) toUi view::consumeState
            viewModel.userOpenCommand toUi router::openUser

            view.userClickIntent fromUi viewModel.userClickIntent
            view.retryIntent fromUi viewModel.retryIntent
            router.loadByIntent fromUi viewModel.loadByIntent
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbind() {
        disposable.clear()
    }
}