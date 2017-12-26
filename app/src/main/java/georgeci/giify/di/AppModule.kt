package georgeci.giify.di

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.erased.bind
import com.github.salomonbrys.kodein.erased.instance
import com.github.salomonbrys.kodein.erased.singleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import georgeci.giify.App
import georgeci.giify.extra.SchedulerFactory
import georgeci.giify.extra.SchedulerFactoryImpl

fun App.diConfig() = Kodein.Module {
    bind<String>(Di.BASE_URL) with instance("https://api.giphy.com")
    bind<String>(Di.GIPHY_API_KEY) with instance("jfFakHISWLT5B7UYiTRvyg9mRLQelmWV")

    bind<SchedulerFactory>() with singleton {
        SchedulerFactoryImpl()
    }

    bind<Gson>() with singleton {
        GsonBuilder()

                .create()
    }
}