package jp.iceserver.icecat.models

abstract class Language
{
    abstract val noPermissionMsg: String
    abstract val playerNotFoundMsg: String
    abstract val gameModeNotFoundMsg: String
    abstract val selectGamemodeMsg: String
    abstract val selectPlayerMsg: String
    abstract val databaseCorrupted: String
    abstract val homeChanged: String
    abstract val teleportedToHome: String
}