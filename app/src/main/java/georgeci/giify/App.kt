package georgeci.giify

import android.support.multidex.MultiDexApplication
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.android.androidModule
import com.github.salomonbrys.kodein.lazy
import georgeci.giify.di.diConfig
import georgeci.giify.di.networkModule
import georgeci.giify.di.useCaseModule

class App : MultiDexApplication(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(networkModule())
        import(diConfig())
        import(useCaseModule())
        import(androidModule)
    }
}