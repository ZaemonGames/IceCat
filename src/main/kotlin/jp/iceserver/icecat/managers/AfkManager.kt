package jp.iceserver.icecat.managers

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.models.AfkMode
import jp.iceserver.icecat.tables.PlayerData
import jp.iceserver.icecat.utils.isAfk
import jp.iceserver.icecat.utils.setNickName
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.math.abs

object AfkManager
{
    private val lang = IceCat.lang

    private val afkPlayers: MutableMap<UUID, AfkMode> = HashMap()
    private val startLocations: MutableMap<UUID, Location> = HashMap()
    private var afkChecker: afkCheckSchedule? = null


    private fun setAfk(player: Player, mode: AfkMode, byAdmin: Boolean)
    {
        val uniqueId = player.uniqueId

        if (afkPlayers.isEmpty())
        {
            afkChecker = afkCheckSchedule()
            afkChecker?.runTaskTimer(IceCat.plugin, 0L, 40L)
        }
        if (mode == AfkMode.SPECTATOR) player.gameMode = GameMode.SPECTATOR
        afkPlayers[uniqueId] = mode
        startLocations[uniqueId] = player.location
        player.setNickName("[AFK]${player.name}")
        player.isSleepingIgnored = true
        if (byAdmin) player.msg(lang.becameAfkByAdminMsg) else player.msg(lang.becameAfkMsg)
    }


    fun removeAfk(player: Player)
    {
        val uniqueId = player.uniqueId
        if (afkPlayers.contains(uniqueId))
        {
            afkPlayers.remove(player.uniqueId)
            player.gameMode = GameMode.SURVIVAL
            player.setNickName(player.name)
            if (player.hasPermission("icecat.command.nickname"))
            {
                transaction {
                    PlayerData.select { PlayerData.uniqueId eq player.uniqueId }.forEach {
                        player.setNickName(it[PlayerData.nickname])
                    }
                }
            }
            player.isSleepingIgnored = false
            if (afkPlayers.isEmpty()) afkChecker?.cancel()
            player.sendActionBar(Component.empty())
            player.msg(lang.afkRemovedMsg)
        }
    }


    fun toggleAfk(player: Player, mode: AfkMode, byAdmin: Boolean)
    {
        if (player.isAfk()) removeAfk(player) else setAfk(player, mode, byAdmin)
    }


    fun isAfk(player: Player): Boolean
    {
        return afkPlayers.contains(player.uniqueId)
    }


    fun getAfkMode(player: Player): AfkMode
    {
        return afkPlayers[player.uniqueId]!!
    }


    private class afkCheckSchedule : BukkitRunnable()
    {
        override fun run()
        {
            afkPlayers.forEach {
                val player = Bukkit.getPlayer(it.key)
                player?.sendActionBar(Component.text("${ChatColor.GREEN}AFK中です！"))
                val startLocation = startLocations[it.key]

                if (player != null && startLocation != null)
                {
                    val currentLocation = player.location
                    if (hasMovedEnough(currentLocation, startLocation)) removeAfk(player)
                }
            }
        }
    }


    private fun hasMovedEnough(start: Location, current: Location): Boolean
    {
        // ここでは、x, y, zのいずれかが1以上動いたかを判定しています。
        return (abs(start.x - current.x) >= 1.0 || abs(start.y - current.y) >= 1.0 || abs(start.z - current.z) >= 1.0)
    }
}