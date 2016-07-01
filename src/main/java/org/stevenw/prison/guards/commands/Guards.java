package org.stevenw.prison.guards.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stevenw.prison.guards.Guard;

import java.util.List;

public class Guards implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        sender.sendMessage(ChatColor.RED + "Guards on duty:");
        List<Player> guards = Guard.guardsOnDuty();
        if(!guards.isEmpty()) {
            for (Player guard : guards)
            {
                sender.sendMessage(ChatColor.AQUA + guard.getName());
            }
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "There are no guards on duty!");
        }
        return true;
    }
}
