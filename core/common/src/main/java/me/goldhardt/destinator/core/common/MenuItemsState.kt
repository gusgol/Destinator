package me.goldhardt.destinator.core.common

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MenuItem(
    val id: String,
    val titleRes: Int,
    val iconRes: Int,
    val action: () -> Unit,
)

class MenuItemsState {
    private val _menuItems = MutableStateFlow<Map<String,List<MenuItem>>>(mapOf())
    val menuItems: StateFlow<Map<String,List<MenuItem>>> = _menuItems

    fun setMenuItems(route: String, menuItems: List<MenuItem>) {
        _menuItems.value += (route to menuItems)
    }
}

val LocalMenuItemState = compositionLocalOf { MenuItemsState() }