package georgeci.giify.model.usecase

import georgeci.giify.model.ImageEntity
import georgeci.giify.model.User
import georgeci.giify.model.source.GiphyApi
import georgeci.giify.model.source.response.ListResponse
import io.reactivex.Single
import java.util.Random

class GetListUseCase(
        private val api: GiphyApi
) {
    private val random = Random()

    fun get(limit: Int, offset: Int): Single<GetListResolution> {
        return api.getList(limit, offset)
                .map<GetListResolution> {
                    //                    if (random.nextBoolean()) {
                    convert(it)
//                    } else {
//                        GetListResolution.Error
//                    }
                }
                .onErrorReturn {
                    it.printStackTrace()
                    GetListResolution.Error
                }
    }

    private fun convert(it: ListResponse): GetListResolution.Success {
        return GetListResolution.Success(
                it.data.map {
                    val im = it.images.im
                    val user = it.user?.let{
                        User(
                                it.avatarPath,
                                it.profile_url,
                                it.username,
                                it.displayName,
                                it.twitter
                        )
                    }
                    ImageEntity(height = im.height.toInt(), width = im.width.toInt(), path = im.url, id = it.id, user = user)
                }
        )
    }
}

sealed class GetListResolution {
    data class Success(val items: List<ImageEntity>) : GetListResolution()
    object Error : GetListResolution()
}
