package georgeci.giify.screen.detail

import georgeci.giify.extra.RxViewModel
import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kategory.Either

abstract class DetailViewModel: RxViewModel() {
    abstract val state: Observable<DetailState>
    abstract val userClickIntent: Consumer<Unit>
    abstract val userOpenCommand: Observable<String>
    abstract val loadByIntent: Consumer<Either<String, ImageEntity>>
    abstract val retryIntent: Consumer<Unit>
}