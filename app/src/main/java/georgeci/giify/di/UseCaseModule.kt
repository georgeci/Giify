package georgeci.giify.di

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.erased.bind
import com.github.salomonbrys.kodein.erased.instance
import com.github.salomonbrys.kodein.erased.singleton
import georgeci.giify.App
import georgeci.giify.model.usecase.GetItemUseCase
import georgeci.giify.model.usecase.GetListUseCase

fun App.useCaseModule() = Kodein.Module {
    bind<GetListUseCase>() with singleton {
        GetListUseCase(api = instance())
    }

    bind<GetItemUseCase>() with singleton {
        GetItemUseCase(api = instance())
    }
}