package org.stevenw.prison.guards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.stevenw.prison.guards.Guard;
import org.stevenw.prison.guards.sGuards;
import org.stevenw.prison.guards.tasks.DutyAdder;
import org.stevenw.prison.guards.tasks.DutyRemover;

public class Duty implements CommandExecutor {
    private sGuards plugin;
    public Duty(sGuards plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Guard guard = new Guard(plugin,player);
        if(guard.isOnDuty())
        {
            int warmup = getWarmup(player);
            player.sendMessage("You will be put off duty in: " + warmup + " seconds!");
            BukkitTask task = new DutyRemover(plugin, guard).runTaskLater(plugin, warmup * 20);
        } else {
            Integer warmup = getWarmup(player);
            player.sendMessage("You will be put on duty in: " + warmup.toString() + " seconds!");
            BukkitTask task = new DutyAdder(plugin, guard).runTaskLater(plugin, warmup * 20);
        }
        return true;
    }
    private int getWarmup(Player player) {
        int warmup = 0;
        if(!player.hasPermission("guards.duty.bypasswarmup"))
        {
            warmup = plugin.getConfig().getInt("off-duty-warmup");
        }
        return warmup;
    }
}
