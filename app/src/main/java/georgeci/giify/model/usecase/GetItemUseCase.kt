package georgeci.giify.model.usecase

import georgeci.giify.model.ImageEntity
import georgeci.giify.model.User
import georgeci.giify.model.source.GiphyApi
import georgeci.giify.model.source.response.ItemResponse
import io.reactivex.Single
import java.util.Random

class GetItemUseCase(
        private val api: GiphyApi
) {
    private val random = Random()

    fun get(id: String): Single<GetItemResolution> {
        return api.getItem(id)
                .map<GetItemResolution> {
                    //                    if (random.nextBoolean()) {
                    convert(it)
//                    } else {
//                        GetListResolution.Error
//                    }
                }
                .onErrorReturn {
                    it.printStackTrace()
                    GetItemResolution.Error
                }
    }

    private fun convert(it: ItemResponse): GetItemResolution.Success {
        val im = it.data.images.im
        val user = it.data.user?.let{
            User(
                    it.avatarPath,
                    it.profile_url,
                    it.username,
                    it.displayName,
                    it.twitter
            )
        }
        val item = ImageEntity(height = im.height.toInt(), width = im.width.toInt(), path = im.url, id = it.data.id, user = user)
        return GetItemResolution.Success(item)
    }
}

sealed class GetItemResolution {
    data class Success(val item: ImageEntity) : GetItemResolution()
    object Error : GetItemResolution()
}