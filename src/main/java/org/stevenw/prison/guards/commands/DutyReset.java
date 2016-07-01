package org.stevenw.prison.guards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stevenw.prison.guards.Guard;
import org.stevenw.prison.guards.sGuards;

public class DutyReset implements CommandExecutor {
    private sGuards plugin;
    public DutyReset(sGuards plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Guard guard = new Guard(plugin, player);
        if(guard.isOnDuty())
        {
            //player is on duty, take them off duty
            guard.setOffDuty();
            guard.setOnDuty();
            player.sendMessage("Your on-duty inventory has been reset!");
            //VulcanGuards.plugin.announceOffDuty(player);
        } else {
            player.sendMessage("You are not on duty!");
        }
        return true;
    }
}
