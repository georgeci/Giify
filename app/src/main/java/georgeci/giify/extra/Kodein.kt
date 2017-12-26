package georgeci.giify.extra

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bindings.NoArgBindingKodein
import com.github.salomonbrys.kodein.bindings.SingletonBinding
import com.github.salomonbrys.kodein.erased

inline fun <reified T : ViewModel> Kodein.Builder.viewModelSingleton(
        activity: BaseActivity,
        noinline creator: NoArgBindingKodein.() -> T
) = SingletonBinding(erased()) {
    val f = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return creator() as T
        }
    }
    ViewModelProviders.of(activity, f)
            .get(T::class.java)
}