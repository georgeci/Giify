package georgeci.giify.screen.list

import georgeci.giify.model.ImageEntity

sealed class ListState {
    sealed class Content : ListState() {
        abstract val list: List<ImageEntity>
        abstract val offset: Int

        data class Idle(override val list: List<ImageEntity>, override val offset: Int) : Content()
        data class ErrorToast(override val list: List<ImageEntity>, override val offset: Int) : Content()
        data class Error(override val list: List<ImageEntity>, override val offset: Int) : Content()
        data class LoadMore(override val list: List<ImageEntity>, override val offset: Int) : Content()
        data class Refresh(override val list: List<ImageEntity>, override val offset: Int) : Content()
    }

    object FullScreenError : ListState()
    object FullScreenProgress : ListState()
}