package me.zhenxin.zmusic.command

object CmdEx {

    fun tab(sender: Any, args: Array<out String>): MutableList<String> {
        println(sender)
        println(args)
        return mutableListOf()
    }

    fun cmd(sender: Any, args: Array<out String>): Boolean {
        println(sender)
        println(args)
        return true
    }
}
