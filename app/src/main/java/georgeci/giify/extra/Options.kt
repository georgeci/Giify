package georgeci.giify.extra

import io.reactivex.Observable
import kategory.Option

fun <T> T?.toOption() = Option.fromNullable(this)

fun <T> Observable<Option<T>>.filterDefined() =
        filter { !it.isEmpty }
                .map { (it as Option.Some).value }