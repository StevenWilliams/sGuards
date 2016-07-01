package org.stevenw.prison.guards.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.stevenw.prison.guards.Guard;
import org.stevenw.prison.guards.sGuards;
import org.stevenw.prison.guards.util.Util;

import java.util.List;

public class DutyAdder extends BukkitRunnable {

    private final sGuards plugin;
    private final Guard guard;

    public DutyAdder(sGuards plugin, Guard guard) {
        this.plugin = plugin;
        this.guard = guard;
    }
    @Override
    public void run() {
        if(!guard.getPlayer().hasPermission("guards.duty.bypasstp")) {
            guard.getPlayer().teleport((Bukkit.getWorlds().get(0)).getSpawnLocation());
        }
        guard.setOnDuty();
        announceOnDuty(guard.getPlayer());
    }
    public void announceOnDuty(Player guard) {
        List<String> dutymsg = plugin.getConfig().getStringList("duty-msg");
        dutymsg = Util.formatMessage(dutymsg, guard);
        for(String msg : dutymsg) {
            Bukkit.broadcastMessage(msg);
        }
    }
}
