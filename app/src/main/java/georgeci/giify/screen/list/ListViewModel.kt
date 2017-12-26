package georgeci.giify.screen.list

import georgeci.giify.extra.RxViewModel
import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import io.reactivex.functions.Consumer

abstract class ListViewModel : RxViewModel() {
    abstract val state: Observable<ListState>
    abstract val routeCommand: Observable<Pair<Int, ImageEntity>>
    abstract val refreshIntent: Consumer<Unit>
    abstract val loadNewPager: Consumer<Unit>
    abstract val itemClickIntent: Consumer<Pair<Int, ImageEntity>>
}