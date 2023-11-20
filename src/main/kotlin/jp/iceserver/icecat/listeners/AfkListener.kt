package jp.iceserver.icecat.listeners

import jp.iceserver.icecat.models.AfkMode
import jp.iceserver.icecat.utils.getAfkMode
import jp.iceserver.icecat.utils.isAfk
import jp.iceserver.icecat.utils.removeAfk
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class AfkListener : Listener
{

    /* ----- afk ----- */
    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent)
    {
        val entity = e.entity

        if (entity is Player && entity.isAfk())
        {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent)
    {
        val player = e.player

        if (player.isAfk()) player.removeAfk()
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent)
    {
        val damager = e.damager
        if (damager is Player && damager.isAfk()) damager.removeAfk()
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent)
    {
        if (e.player.isAfk() && e.player.getAfkMode() == AfkMode.SPECTATOR) e.isCancelled = true
    }
}