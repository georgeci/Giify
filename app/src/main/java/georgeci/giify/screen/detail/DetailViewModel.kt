package georgeci.giify.screen.detail

import georgeci.giify.model.ImageEntity
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kategory.Either

interface DetailViewModel {
    val state: Observable<DetailState>
    val userClickIntent: Consumer<Unit>
    val userOpenCommand: Observable<String>
    val loadByIntent: Consumer<Either<String, ImageEntity>>
    val retryIntent: Consumer<Unit>
}