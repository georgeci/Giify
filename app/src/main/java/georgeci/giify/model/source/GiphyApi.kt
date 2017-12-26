package georgeci.giify.model.source

import georgeci.giify.model.source.response.ItemResponse
import georgeci.giify.model.source.response.ListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {
    //    http://api.giphy.com
    @GET("/v1/gifs/trending?fmt=json")
    fun getList(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<ListResponse>

    @GET("/v1/gifs/{id}?fmt=json")
    fun getItem(@Path("id") id: String): Single<ItemResponse>
}