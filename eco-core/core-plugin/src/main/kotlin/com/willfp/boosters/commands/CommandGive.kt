package com.willfp.boosters.commands

import com.willfp.boosters.boosters.Boosters
import com.willfp.boosters.getAmountOfBooster
import com.willfp.boosters.setAmountOfBooster
import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.TabCompleteHandler
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandGive(plugin: EcoPlugin) :
    Subcommand(
        plugin,
        "give",
        "boosters.command.give",
        false
    ) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { sender: CommandSender, args: List<String> ->
            if (args.isEmpty()) {
                sender.sendMessage(plugin.langYml.getMessage("requires-player"))
                return@CommandHandler
            }

            if (args.size == 1) {
                sender.sendMessage(plugin.langYml.getMessage("requires-booster"))
                return@CommandHandler
            }

            val booster = Boosters.getById(args[1].lowercase())

            if (booster == null) {
                sender.sendMessage(plugin.langYml.getMessage("invalid-booster"))
                return@CommandHandler
            }

            this.plugin.scheduler.runAsync {
                @Suppress("DEPRECATION")
                val player = Bukkit.getOfflinePlayer(args[0])
                if (!player.hasPlayedBefore()) {
                    sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
                    return@runAsync
                }

                this.plugin.scheduler.run {
                    player.setAmountOfBooster(booster, player.getAmountOfBooster(booster) + 1)
                }

                sender.sendMessage(
                    plugin.langYml.getMessage("gave-booster", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                        .replace("%player%", player.name?: return@runAsync)
                        .replace("%booster%", booster.id)
                )
            }
        }
    }

    override fun getTabCompleter(): TabCompleteHandler {
        return TabCompleteHandler { _, args ->
            val completions = mutableListOf<String>()

            if (args.size == 1) {
                StringUtil.copyPartialMatches(
                    args[0],
                    Bukkit.getOnlinePlayers().map { player -> player.name }.toCollection(ArrayList()),
                    completions
                )
                return@TabCompleteHandler completions
            }

            if (args.size == 2) {
                StringUtil.copyPartialMatches(
                    args[1],
                    Boosters.names(),
                    completions
                )
                return@TabCompleteHandler completions
            }

            return@TabCompleteHandler emptyList<String>()
        }
    }
}