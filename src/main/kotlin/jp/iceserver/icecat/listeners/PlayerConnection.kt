package jp.iceserver.icecat.listeners

import jp.iceserver.icecat.tables.HomeData
import jp.iceserver.icecat.tables.PlayerData
import jp.iceserver.icecat.utils.setNickName
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerConnection : Listener
{
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent)
    {
        val player = e.player

        /* ----- nickname -----*/
        transaction {
            if (!player.hasPermission("icecat.command.nickname")) return@transaction
            PlayerData.select { PlayerData.uniqueId eq player.uniqueId }.forEach {
                player.setNickName(it[PlayerData.nickname])
            }
        }


        /* ----- home -----*/
        transaction {
            if (!player.hasPermission("icecat.command.sethome")) return@transaction
            val exist = HomeData.select { HomeData.uniqueId eq player.uniqueId }.any()

            if (!exist)
            {
                val worldSpawn = Bukkit.getWorld("world")?.spawnLocation ?: return@transaction
                HomeData.insert {
                    it[uniqueId] = player.uniqueId
                    it[x] = worldSpawn.x
                    it[y] = worldSpawn.y
                    it[z] = worldSpawn.z
                    it[yaw] = worldSpawn.yaw
                    it[pitch] = worldSpawn.pitch
                }
            }
        }
    }
}