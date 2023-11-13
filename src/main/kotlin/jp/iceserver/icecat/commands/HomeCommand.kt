package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.HomeData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class HomeCommand : CommandExecutor, TabCompleter
{
    private val lang = IceCat.lang

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean
    {
        sender as Player

        if (!sender.hasPermission("icecat.command.home")) return sender.msg(lang.noPermissionMsg).let { true }
        transaction {
            HomeData.select { (HomeData.uniqueId eq sender.uniqueId) }.forEach {
                val world = Bukkit.getWorld("world")
                val x = it[HomeData.x]
                val y = it[HomeData.y]
                val z = it[HomeData.z]
                val yaw = it[HomeData.yaw]
                val pitch = it[HomeData.pitch]
                if (world != null)
                {
                    val home = Location(world, x, y, z, yaw, pitch)
                    sender.teleport(home)
                    sender.msg(lang.teleportedToHome)
                }
            }
        }
        return true
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>
    {
        return emptyList()
    }
}