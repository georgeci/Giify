package georgeci.giify.screen.detail

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import georgeci.giify.extra.RxViewModel
import georgeci.giify.extra.SchedulerFactory
import georgeci.giify.model.ImageEntity
import georgeci.giify.model.usecase.GetItemResolution
import georgeci.giify.model.usecase.GetItemUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import kategory.Either

class DetailViewModelImpl(
        private val useCase: GetItemUseCase,
        private val schedulers: SchedulerFactory
) : RxViewModel(), DetailViewModel {
    override val state = BehaviorRelay.createDefault<DetailState>(DetailState.Waiting)
    override val userClickIntent = PublishRelay.create<Unit>()
    override val userOpenCommand = PublishRelay.create<String>()
    override val retryIntent = PublishRelay.create<Unit>()
    override val loadByIntent = PublishRelay.create<Either<String, ImageEntity>>()

    init {
        disposable += state
                .distinctUntilChanged()
                .switchMap { oldState ->
                    when (oldState) {
                        DetailState.Waiting -> subscribeLoadByIntent(oldState)
                        is DetailState.Loading -> subscribeDataLoading(oldState)
                        is DetailState.Error -> Observable.merge(
                                subscribeLoadByIntent(oldState),
                                subscribeDataLoadingRetry(oldState)
                        )
                        is DetailState.Content -> {
                            val sideEffect = userClickIntent.map { oldState.item.user!!.profile_url }.subscribe(userOpenCommand)
                            subscribeLoadByIntent(oldState).doOnDispose{
                                sideEffect.dispose()
                            }
                        }
                    }
                }
                .subscribe(state)
    }

    private fun subscribeDataLoadingRetry(oldState: DetailState.Error): Observable<DetailState> {
        return useCase.get(oldState.id)
                .map<DetailState> { resolution ->
                    when (resolution) {
                        is GetItemResolution.Success -> DetailState.Content(resolution.item)
                        is GetItemResolution.Error -> DetailState.Error(oldState.id)
                    }
                }
                .toObservable()
    }

    private fun subscribeDataLoading(oldState: DetailState.Loading): Observable<DetailState> {
        return useCase.get(oldState.id)
                .map<DetailState> { resolution ->
                    when (resolution) {
                        is GetItemResolution.Success -> DetailState.Content(resolution.item)
                        is GetItemResolution.Error -> DetailState.Error(oldState.id)
                    }
                }
                .toObservable()
    }

    private fun subscribeLoadByIntent(oldState: DetailState): Observable<DetailState> {
        return loadByIntent.map {
            it.fold(
                    { DetailState.Loading(it) },
                    { DetailState.Content(it) }
            )
        }
    }
}