package georgeci.giify.screen.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.erased.provider
import com.github.vmironov.jetpack.arguments.bindOptionalArgument
import georgeci.giify.R
import georgeci.giify.extra.BaseActivity
import georgeci.giify.extra.CacheRelay
import georgeci.giify.model.ImageEntity

class DetailActivity : BaseActivity() {

    companion object {
        const val IMAGE_KEY = "image key"
        const val TRANSITION_NAME = "trans key"
        fun intent(context: Context, imageEntity: ImageEntity) =
                Intent(context, DetailActivity::class.java).putExtra(IMAGE_KEY, imageEntity)
    }

    private val transitionName by bindOptionalArgument<String>(TRANSITION_NAME, null)

    private val bindingProvider by provider<DetailBinding>()

    private val intentStream = CacheRelay.create<Intent>()

    override fun provideOverridingModule() = diModule(intentStream, transitionName)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentStream.accept(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        lifecycle.addObserver(bindingProvider())
        intentStream.accept(intent)

        transitionName?.apply { supportPostponeEnterTransition() }
    }
}