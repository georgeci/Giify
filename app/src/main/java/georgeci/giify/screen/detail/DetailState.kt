package georgeci.giify.screen.detail

import georgeci.giify.model.ImageEntity

sealed class DetailState {
    object Waiting : DetailState()
    data class Loading(val id: String) : DetailState()
    data class Error(val id: String) : DetailState()
    data class Content(val item: ImageEntity) : DetailState()
}