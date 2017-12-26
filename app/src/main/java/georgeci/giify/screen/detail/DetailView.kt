package georgeci.giify.screen.detail

import io.reactivex.Observable

interface DetailView {
    fun consumeState(state: DetailState)
    val userClickIntent: Observable<Unit>
    val retryIntent: Observable<Unit>
}