package org.stevenw.prison.guards;

import org.bukkit.entity.Player;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.prison.guards.commands.Duty;
import org.stevenw.prison.guards.commands.DutyReset;
import org.stevenw.prison.guards.commands.Guards;
import org.stevenw.prison.guards.listeners.GuardListener;

import java.util.List;

public class sGuards extends JavaPlugin {
    public Permission getPerms() {
        return perms;
    }

    private Permission perms;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("duty").setExecutor(new Duty(this));
        this.getCommand("dutyreset").setExecutor(new DutyReset(this));
        this.getCommand("guards").setExecutor(new Guards());
        this.getServer().getPluginManager().registerEvents(new GuardListener(this), this);
        setupPermissions();

    }
    @Override
    public void onDisable() {
        List<Player> guards = Guard.guardsOnDuty();
        for(Player player : guards){
            Guard guard = new Guard(this, player);
            guard.setOffDuty();
        }
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

}
