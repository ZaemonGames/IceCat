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
    abstract val homeHeightChangedMsg: String
    abstract val teleportedToHomeMsg: String
    abstract val teleportedToDeathPointMsg: String
    abstract val deathPointNotFoundMsg: String
    abstract val deathDetectedMsg: String
    abstract val deathPointHeightChangedMsg: String
    abstract val afkModeNotFoundMsg: String
    abstract val becameAfkMsg: String
    abstract val becameAfkByAdminMsg: String
    abstract val afkRemovedMsg: String
    abstract val invalidHomeWorldMsg: String
    abstract val becameStaffMsg: String
    abstract val selectedPlayerBecameStaffMsg: String
    abstract val noLongerStaffMsg: String
    abstract val selectedPlayerNoLongerStaffMsg: String
}