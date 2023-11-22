package jp.iceserver.icecat.config

import hazae41.minecraft.kutils.bukkit.ConfigSection
import hazae41.minecraft.kutils.bukkit.PluginConfigFile

object MainConfig : PluginConfigFile("config")
{
    var language by string("lang")
    var prefix by string("prefix")
    var reportContents by stringList("reportContents")
    var webhookUrl by string("webhookUrl")
    var formId by string("formId")

    object MysqlSection : ConfigSection(MainConfig, "mysql")
    {
        val host by string("host")
        val port by string("port")
        val user by string("user")
        val pass by string("pass")
        val name by string("name")
    }
}
