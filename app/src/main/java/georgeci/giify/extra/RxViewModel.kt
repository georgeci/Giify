package georgeci.giify.extra

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class RxViewModel : ViewModel() {

    protected val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
    }
}