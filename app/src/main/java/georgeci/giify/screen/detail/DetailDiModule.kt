package georgeci.giify.screen.detail

import android.content.Intent
import android.view.View
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.erased.bind
import com.github.salomonbrys.kodein.erased.instance
import com.github.salomonbrys.kodein.erased.provider
import com.github.salomonbrys.kodein.erased.singleton
import georgeci.giify.extra.viewModelSingleton
import georgeci.giify.screen.list.ListViewModel
import georgeci.giify.screen.list.ListViewModelImpl
import georgeci.giify.screen.list.diModule
import io.reactivex.Observable

fun DetailActivity.diModule(intentStream: Observable<Intent>, transitionName: String?) = Kodein.Module {
    bind<View>() with provider {
        window.decorView
    }

    bind<DetailRouter>() with singleton {
        DetailRouterImpl(activity = this@diModule, intentStream = intentStream)
    }

    bind<DetailView>() with singleton {
        DetailViewImpl(
                view = window.decorView,
                activity = this@diModule,
                transitionName = transitionName
        )
    }

    bind<DetailViewModel>() with viewModelSingleton(this@diModule) {
        DetailViewModelImpl(
                useCase = instance(),
                schedulers = instance()
        )
    }

    bind<DetailBinding>() with singleton {
        DetailBinding(
                view = instance(),
                viewModel = instance(),
                router = instance(),
                schedulers = instance()
        )
    }
}