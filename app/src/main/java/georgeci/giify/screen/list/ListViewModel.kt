package georgeci.giify.screen.list

import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface ListViewModel {
    val state: Observable<ListState>
    val routeCommand: Observable<Pair<Int, ImageEntity>>
    val refreshIntent: Consumer<Unit>
    val loadNewPager: Consumer<Unit>
    val itemClickIntent: Consumer<Pair<Int, ImageEntity>>
}