package georgeci.giify.extra

import io.reactivex.Scheduler
import java.util.concurrent.ExecutorService

interface SchedulerFactory {
    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun from(executorService: ExecutorService): Scheduler
    fun newThread(): Scheduler
    fun single(): Scheduler
}