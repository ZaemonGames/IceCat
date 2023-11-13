package jp.iceserver.icecat.config

import hazae41.minecraft.kutils.bukkit.PluginConfigFile

object MainConfig : PluginConfigFile("config")
{
    var language by string("lang")
    var prefix by string("prefix")
    var reportContents by stringList("reportContents")
    var webhookUrl by string("webhookUrl")
    var formId by string("formId")
}