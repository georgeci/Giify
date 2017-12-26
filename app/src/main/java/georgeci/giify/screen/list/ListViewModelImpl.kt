package georgeci.giify.screen.list

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import georgeci.giify.extra.RxViewModel
import georgeci.giify.extra.SchedulerFactory
import georgeci.giify.model.usecase.GetListResolution
import georgeci.giify.model.usecase.GetListUseCase
import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit

class ListViewModelImpl(
        private val useCase: GetListUseCase,
        private val schedulers: SchedulerFactory,
        private val limit: Int = 33
) :  ListViewModel() {
    override val state = BehaviorRelay.createDefault<ListState>(ListState.FullScreenProgress)
    override val routeCommand = PublishRelay.create<Pair<Int, ImageEntity>>()
    override val refreshIntent = PublishRelay.create<Unit>()
    override val loadNewPager = PublishRelay.create<Unit>()
    override val itemClickIntent = PublishRelay.create<Pair<Int, ImageEntity>>()

    init {
        disposable += itemClickIntent.subscribe(routeCommand)
        disposable += state
                .distinctUntilChanged()
                .switchMap { oldState ->
                    Log.i("QOQ", "execution: $oldState")
                    when (oldState) {
                        is ListState.Content.Idle -> {
                            Observable.merge(
                                    subscribeRefreshIntent(oldState),
                                    subscribeNewPageIntent(oldState)
                            )
                        }
                        is ListState.Content.LoadMore -> subscribeDataLoading(oldState)
                        is ListState.Content.ErrorToast -> Observable.just(ListState.Content.Error(oldState.list, oldState.offset))
                        is ListState.Content.Error -> Observable.merge(
                                subscribeRefreshIntent(oldState),
                                subscribeNewPageIntent(oldState)
                        )
                        ListState.FullScreenError -> subscribeRefreshIntent(oldState)
                        ListState.FullScreenProgress -> subscribeRefreshing(oldState)
                        is ListState.Content.Refresh -> subscribeRefreshing(oldState)
                    }
                }
                .subscribe(state)
    }

    private fun subscribeRefreshing(oldState: ListState): Observable<ListState> {
        return useCase.get(offset = 0, limit = limit)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.computation())
                .map { resolution ->
                    when (oldState) {
                        is ListState.FullScreenProgress -> {
                            when (resolution) {
                                is GetListResolution.Success -> ListState.Content.Idle(resolution.items, offset = limit)
                                is GetListResolution.Error -> ListState.FullScreenError
                            }
                        }
                        is ListState.Content.Refresh -> {
                            when (resolution) {
                                is GetListResolution.Success -> ListState.Content.Idle(resolution.items, offset = limit)
                                is GetListResolution.Error -> ListState.Content.ErrorToast(oldState.list, oldState.offset)
                            }
                        }
                        else -> error("invalid state action")
                    }
                }
                .toObservable()
    }

    private fun subscribeDataLoading(oldState: ListState.Content.LoadMore): Observable<ListState> {
        return useCase.get(offset = oldState.offset, limit = limit)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.computation())
                .map<ListState> { resolution ->
                    when (resolution) {
                        is GetListResolution.Success -> ListState.Content.Idle(oldState.list + resolution.items, offset = oldState.offset + limit)
                        is GetListResolution.Error -> ListState.Content.ErrorToast(oldState.list, oldState.offset)
                    }
                }
                .toObservable()
    }


    private fun subscribeRefreshIntent(oldState: ListState): Observable<ListState> {
        return refreshIntent.map<ListState> {
            when (oldState) {
                is ListState.Content -> ListState.Content.Refresh(oldState.list, oldState.offset)
                is ListState.FullScreenError -> ListState.FullScreenProgress
                else -> error("invalid state action")
            }
        }
    }

    private fun subscribeNewPageIntent(oldState: ListState.Content): Observable<ListState> {
        return loadNewPager.debounce(200, TimeUnit.MILLISECONDS)
                .let {
                    if(oldState is ListState.Content.Error){
                        it.skip(1)
                    }else{
                        it
                    }
                }
                .map { ListState.Content.LoadMore(oldState.list, oldState.offset) }
    }
}