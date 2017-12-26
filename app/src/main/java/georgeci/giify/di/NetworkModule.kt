package georgeci.giify.di

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.erased.bind
import com.github.salomonbrys.kodein.erased.instance
import com.github.salomonbrys.kodein.erased.singleton
import georgeci.giify.App
import georgeci.giify.extra.ApiKeyInterceptor
import georgeci.giify.model.source.GiphyApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun App.networkModule() = Kodein.Module {

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor(apiKey = instance(Di.GIPHY_API_KEY)))
                .build()
    }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
                .baseUrl(instance<String>(Di.BASE_URL))
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(instance()))
                .build()
    }

    bind<GiphyApi>() with singleton {
        instance<Retrofit>().create(GiphyApi::class.java)
    }

    bind<Glide>() with singleton {
        GlideBuilder()
                .build(instance())
    }
}