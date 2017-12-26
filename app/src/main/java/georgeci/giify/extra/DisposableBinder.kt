package georgeci.giify.extra

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

inline fun CompositeDisposable.bind(schedulers: SchedulerFactory, init: Binder.() -> Unit) = Binder(schedulers).apply {
    init()
    bindings.forEach { this@bind.add(it) }
}

class Binder(
        private val schedulers: SchedulerFactory
) {
    val bindings: MutableList<Disposable> = arrayListOf()

    infix fun <T> Observable<T>.toUi(consumer: (T) -> Unit) {
        bindings += this.observeOn(schedulers.ui()).subscribe(consumer)
    }

    infix fun <T> Observable<T>.fromUi(consumer: (T) -> Unit) {
        bindings += this.observeOn(schedulers.computation()).subscribe(consumer)
    }

    infix fun <T> Observable<T>.toUi(consumer: Consumer<T>) {
        bindings += this.observeOn(schedulers.ui()).subscribe(consumer)
    }

    infix fun <T> Observable<T>.fromUi(consumer: Consumer<T>) {
        bindings += this.observeOn(schedulers.computation()).subscribe(consumer)
    }

    infix fun Observable<Unit>.toUi(consumer: () -> Unit) {
        bindings += this.observeOn(schedulers.ui()).subscribe { consumer() }
    }

    infix fun Observable<Unit>.fromUi(consumer: () -> Unit) {
        bindings += this.observeOn(schedulers.computation()).subscribe { consumer() }
    }
}