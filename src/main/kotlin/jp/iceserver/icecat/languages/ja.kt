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

    override val homeHeightChangedMsg = "$prefix 設定地点が空中だったため、一番近い地面に高さを変更しました。"

    override val teleportedToHomeMsg = "$prefix ホームにテレポートしました。"

    override val teleportedToDeathPointMsg = "$prefix 死亡ポイントにテレポートしました。"

    override val deathPointNotFoundMsg = "$errPrefix 死亡ポイントが見つかりません。"

    override val deathDetectedMsg = "$prefix 死亡を検出しました。\n" +
            "${" ".repeat(prefix.length + 2)}1度だけ死亡ポイントに戻ることができます: ${ChatColor.YELLOW}/death"

    override val deathPointHeightChangedMsg = "$prefix 死亡地点が空中だったため、一番近い地面に高さを変更しました。"
}