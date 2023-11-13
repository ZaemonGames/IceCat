package jp.iceserver.icecat.models

abstract class Language
{
    abstract val noPermissionMsg: String
    abstract val playerNotFoundMsg: String
    abstract val gameModeNotFoundMsg: String
    abstract val selectGamemodeMsg: String
    abstract val selectPlayerMsg: String
    abstract val databaseCorruptedMsg: String
    abstract val homeChangedMsg: String
    abstract val teleportedToHomeMsg: String
    abstract val teleportedToDeathPointMsg: String
    abstract val deathPointNotFoundMsg: String
    abstract val deathDetectedMsg: String
    abstract val deathPointHeightChangedMsg: String
}