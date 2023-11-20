package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.models.AfkMode
import jp.iceserver.icecat.utils.toggleAfk
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class AfkCommand : CommandExecutor, TabCompleter
{
    val lang = IceCat.lang

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        sender as Player
        if (!sender.hasPermission("icecat.command.afk")) return sender.msg(lang.noPermissionMsg).let { true }

        if (args.isEmpty()) sender.toggleAfk(AfkMode.PLAYER, false)

        if (args.size == 1)
        {
            try
            {
                val mode = AfkMode.valueOf(args[0].uppercase())
                sender.toggleAfk(mode, false)
            } catch (e: IllegalArgumentException)
            {
                sender.msg(lang.afkModeNotFoundMsg)
            }
        }

        if (args.size == 2)
        {

            try
            {
                val mode = AfkMode.valueOf(args[0].uppercase())
                val player = Bukkit.getPlayer(args[1]) ?: return true
                player.toggleAfk(mode, true)
            } catch (e: IllegalArgumentException)
            {
                sender.msg(lang.afkModeNotFoundMsg)
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
        if (args.size == 1)
        {
            return listOf("player", "spectator").toList().filter {
                it.startsWith(args[0], ignoreCase = true)
            }
        }
        if (args.size == 2)
        {
            if (sender.hasPermission("icecat.command.afk.other")) return Bukkit.getOnlinePlayers().map { it.name }
                .filter { it.startsWith(args[1], ignoreCase = true) }
            return emptyList()
        }
        return emptyList()
    }

}