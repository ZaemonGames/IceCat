package jp.iceserver.icecat.languages

import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.models.Language
import org.bukkit.ChatColor

object ja : Language()
{
    private val prefix = MainConfig.prefix
    private val errPrefix = "$prefix ${ChatColor.RED}[エラー]"

    override val noPermissionMsg = "$errPrefix あなたに実行する権限がありません。"
    
    override val playerNotFoundMsg = "$errPrefix 指定されたプレイヤーが見つかりません。"
    
    override val gameModeNotFoundMsg = "$errPrefix 指定されたゲームモードは存在しません。"
    
    override val selectGamemodeMsg = "$errPrefix ゲームモードを指定してください。"
    
    override val selectPlayerMsg = "$errPrefix プレイヤーを指定してください。"

    override val databaseCorruptedMsg = "$errPrefix データベースが破損しています。"

    override val homeChangedMsg = "$prefix ホームが現在地に更新されました。"

    override val teleportedToHomeMsg = "$prefix ホームにテレポートしました。"
}