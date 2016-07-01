package org.stevenw.prison.guards.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.stevenw.prison.guards.Guard;
import org.stevenw.prison.guards.sGuards;
import org.stevenw.prison.guards.util.Util;

import java.util.List;

public class DutyRemover extends BukkitRunnable {
    private final sGuards plugin;
    private final Guard guard;

    public DutyRemover(sGuards plugin, Guard guard) {
        this.plugin = plugin;
        this.guard = guard;
    }
    @Override
    public void run() {
        guard.setOffDuty();
        //VulcanGuards.plugin.updateNametag(guard.getPlayer());
        announceOffDuty(guard.getPlayer());
    }

    public void announceOffDuty(Player guard) {
        List<String> offdutymsgs = plugin.getConfig().getStringList("off-duty-msg");
        offdutymsgs = Util.formatMessage(offdutymsgs, guard);
        for(String msg : offdutymsgs) {
            plugin.getServer().broadcastMessage(msg);
        }
    }

}
