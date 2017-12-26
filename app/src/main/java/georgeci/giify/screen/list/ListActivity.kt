package georgeci.giify.screen.list

import android.annotation.SuppressLint
import android.os.Bundle
import com.github.salomonbrys.kodein.erased.provider
import georgeci.giify.R
import georgeci.giify.extra.BaseActivity

class ListActivity : BaseActivity() {
    private val bindingProvider by provider<ListBinding>()

    override fun provideOverridingModule() = diModule()

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
        lifecycle.addObserver(bindingProvider())
    }
}