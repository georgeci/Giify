package georgeci.giify.extra

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService

class SchedulerFactoryImpl : SchedulerFactory {
    override fun ui() = AndroidSchedulers.mainThread()

    override fun io() = Schedulers.io()

    override fun computation() = Schedulers.computation()

    override fun trampoline() = Schedulers.trampoline()

    override fun newThread() = Schedulers.newThread()

    override fun single() = Schedulers.single()

    override fun from(executorService: ExecutorService) = Schedulers.io()

}