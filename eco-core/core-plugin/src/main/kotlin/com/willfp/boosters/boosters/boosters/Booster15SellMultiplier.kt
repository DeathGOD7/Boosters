package com.willfp.boosters.boosters.boosters

import com.willfp.boosters.BoostersPlugin
import com.willfp.boosters.activeBooster
import com.willfp.boosters.boosters.Booster
import dev.norska.dsw.api.DeluxeSellwandSellEvent
import net.brcdev.shopgui.event.ShopPreTransactionEvent
import net.brcdev.shopgui.shop.ShopManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class Booster15SellMultiplier: Booster(
    BoostersPlugin.instance,
    "1_5sell_multiplier"
) {
    override val duration = 72000

    @EventHandler(priority = EventPriority.HIGH)
    fun handle(event: ShopPreTransactionEvent) {
        if (Bukkit.getServer().activeBooster?.booster != this) {
            return
        }

        if (event.isCancelled) {
            return
        }

        if (event.shopAction == ShopManager.ShopAction.BUY) {
            return
        }

        event.price *= 1.5
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun handle(event: DeluxeSellwandSellEvent) {
        if (Bukkit.getServer().activeBooster?.booster != this) {
            return
        }

        if (event.isCancelled) {
            return
        }

        event.money *= 1.5
    }
}