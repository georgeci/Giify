package georgeci.giify.screen.list

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.erased.bind
import com.github.salomonbrys.kodein.erased.instance
import com.github.salomonbrys.kodein.erased.provider
import com.github.salomonbrys.kodein.erased.singleton
import georgeci.giify.extra.viewModelSingleton
import georgeci.giify.screen.list.adapter.ListAdapter

fun ListActivity.diModule() = Kodein.Module {
    bind<View>() with provider {
        window.decorView
    }

    bind<ListAdapter>() with singleton {
        ListAdapter(context = instance(), glide = instance())
    }

    bind<ListRouter>() with singleton {
        ListRouterImpl(activity = this@diModule, adapter = instance())
    }

    bind<RecyclerView.LayoutManager>() with singleton {
        StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    bind<ListView>() with singleton {
        ListViewImpl(
                view = window.decorView,
                adapter = instance(),
                layoutManager = instance()
        )
    }

    bind<ListViewModel>() with viewModelSingleton(this@diModule) {
        ListViewModelImpl(
                useCase = instance(),
                schedulers = instance()
        )
    }

    bind<ListBinding>() with singleton {
        ListBinding(
                view = instance(),
                viewModel = instance(),
                router = instance(),
                schedulers = instance()
        )
    }
}