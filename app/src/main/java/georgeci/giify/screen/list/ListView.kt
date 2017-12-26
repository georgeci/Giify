package georgeci.giify.screen.list

import georgeci.giify.model.ImageEntity
import io.reactivex.Observable

interface ListView {
    fun consumeState(state: ListState)
    val refreshIntent: Observable<Unit>
    val loadNewPager: Observable<Unit>
    val itemClickIntent: Observable<Pair<Int, ImageEntity>>
}