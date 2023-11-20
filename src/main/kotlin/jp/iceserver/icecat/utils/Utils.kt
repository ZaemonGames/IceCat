package jp.iceserver.icecat.utils

import jp.iceserver.icecat.managers.AfkManager
import jp.iceserver.icecat.models.AfkMode
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.math.floor

fun OfflinePlayer.getPlayerHead(): ItemStack
{
    val itemStack = ItemStack(Material.PLAYER_HEAD)
    val skull = itemStack.itemMeta as SkullMeta

    skull.displayName(Component.text("${ChatColor.WHITE}${this.name ?: "Unknown"}"))
    skull.owningPlayer = this
    itemStack.itemMeta = skull

    return itemStack
}

fun Player.setNickName(name: String)
{
    val coloredName = Component.text(ChatColor.translateAlternateColorCodes('&', name))
    this.displayName(coloredName)
    this.playerListName(coloredName)
}

fun Int.getSlotPos(): Pair<Int, Int>
    = Pair(floor(this.toDouble() / 9).toInt(), (this - floor(this.toDouble() / 9) * 9).toInt())

fun Player.isAfk(): Boolean
{
    return AfkManager.isAfk(this.player!!)
}

fun Player.removeAfk()
{
    AfkManager.removeAfk(this.player!!)
}

fun Player.toggleAfk(mode: AfkMode, byAdmin: Boolean)
{
    AfkManager.toggleAfk(this.player!!, mode, byAdmin)
}

fun Player.getAfkMode(): AfkMode
{
    return AfkManager.getAfkMode(this.player!!)
}