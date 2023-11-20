package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.config.MainConfig
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class GamemodeCommand : CommandExecutor, TabCompleter
{

    private val prefix = MainConfig.prefix
    private val lang = IceCat.lang

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        if (args.isEmpty())
        {
            sender.msg(lang.selectGamemodeMsg)
            return true
        }

        val modeName = args[0]

        if (sender == Bukkit.getConsoleSender())
        {
            if (args.size < 2)
            {
                sender.msg(lang.selectPlayerMsg)
                return true
            }
            setGamemode(sender, modeName, Bukkit.getPlayerExact(args[1]))
            return true
        }

        sender as Player
        if (args.size == 1) setGamemode(sender, modeName)
        if (args.size == 2) setGamemode(sender, modeName, Bukkit.getPlayerExact(args[1]))

        return true
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>
    {
        if (args.size == 1)
        {
            val availableModes = mutableListOf<String>()
            if (sender.hasPermission("icecat.command.gamemode.survival")) availableModes.add("survival")
            if (sender.hasPermission("icecat.command.gamemode.creative")) availableModes.add("creative")
            if (sender.hasPermission("icecat.command.gamemode.adventure")) availableModes.add("adventure")
            if (sender.hasPermission("icecat.command.gamemode.spectator")) availableModes.add("spectator")
            return availableModes.filter {
                it.startsWith(args[0], ignoreCase = true)
            }
        } else if (args.size == 2)
        {
            if (sender.hasPermission("icecat.command.gamemode.other")) return Bukkit.getOnlinePlayers().map { it.name }
                .filter {
                    it.startsWith(args[1], ignoreCase = true)
                }
        }
        return emptyList()
    }


    private fun setGamemode(player: Player, name: String)
    {
        try
        {
            val gameMode = GameMode.valueOf(name.uppercase())
            if (player.gameMode == gameMode)
            {
                player.msg("$prefix ${ChatColor.RED}あなたは既に${gameMode.name}です。")
                return
            }
            if (!player.hasPermission("icecat.command.gamemode.${gameMode.name.lowercase()}"))
            {
                player.msg(lang.noPermissionMsg)
                return
            }
            player.gameMode = gameMode
            player.msg("$prefix ゲームモードを${gameMode.name}に変更しました。")
        } catch (e: IllegalArgumentException)
        {
            player.msg(lang.gameModeNotFoundMsg)
            return
        }
    }


    private fun setGamemode(player: CommandSender, name: String, target: Player?)
    {
        try
        {
            val gameMode = GameMode.valueOf(name.uppercase())

            if (target == null)
            {
                player.msg(lang.playerNotFoundMsg)
                return
            }

            if (player == Bukkit.getConsoleSender())
            {
                target.gameMode = gameMode
                player.msg("$prefix ${target.name}のゲームモードを${gameMode.name}に変更しました。")
                Bukkit.getOnlinePlayers().filter { it.hasPermission("group.staff") && it != player }
                    .forEach { if (it != target && it != player) it.msg("$prefix サーバーが${target.name}のゲームモードを${gameMode.name}に変更しました。") }
                target.msg("$prefix サーバーがあなたのゲームモードを${gameMode.name}に変更しました。")
                return
            }

            if (!player.hasPermission("icecat.command.gamemode.other.${gameMode.name.lowercase()}"))
            {
                player.msg(lang.noPermissionMsg)
                return
            }

            player as Player

            if (player.gameMode == gameMode)
            {
                player.msg("$prefix ${ChatColor.RED}${target.name}は既に${gameMode.name}です。")
                return
            }

            target.gameMode = gameMode
            player.msg("$prefix ${target.name}のゲームモードを${gameMode.name}に変更しました。")
            Bukkit.getOnlinePlayers().filter { it.hasPermission("group.staff") && it != player }
                .forEach { if (it != target && it != player) it.msg("$prefix ${player.name}が${target.name}を${gameMode.name}に変更しました。") }
            if (target != player) target.msg("$prefix ${player.name}があなたのゲームモードを${gameMode.name}に変更しました。")
        } catch (e: IllegalArgumentException)
        {
            player.msg(lang.gameModeNotFoundMsg)
            return
        }
    }
}
