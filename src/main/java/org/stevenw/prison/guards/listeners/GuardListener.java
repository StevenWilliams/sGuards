package org.stevenw.prison.guards.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.stevenw.prison.guards.Guard;
import org.stevenw.prison.guards.sGuards;
import org.stevenw.prison.guards.util.Util;

import java.util.List;

public class GuardListener implements Listener {
    private sGuards plugin;
    public GuardListener(sGuards plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Guard guard = new Guard(plugin, event.getPlayer());
        if (guard.isOnDuty()) {
            guard.setOffDuty();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Guard guard = new Guard(plugin, event.getEntity());
        if (guard.isOnDuty()) {
            announceGuardDeathMessage(player);
            event.getDrops().clear();
            event.setDeathMessage(null);

            guard.setOffDuty();

            guard.setOnDuty();
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        final Guard guard = new Guard(plugin, event.getPlayer());
        if (guard.isOnDuty()) {
            guard.setOffDuty();
            guard.setOnDuty();
            new BukkitRunnable(){
                @Override
                public void run() {
                    guard.setGuardEffects();
                }
            }.runTaskLater(plugin, 20);
        }
    }

    //cancel events
    @EventHandler
    public void onItemDropEvent(PlayerDropItemEvent event) {
        Guard guard = new Guard(plugin, event.getPlayer());
        if (guard.isOnDuty()) {
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Guard guard = new Guard(plugin, event.getPlayer());
        if (guard.isOnDuty()) {
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Guard guard = new Guard(plugin, event.getPlayer());
        if (guard.isOnDuty()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
            Guard victim = new Guard(plugin, (Player) event.getEntity());
            Guard attacker = new Guard(plugin, (Player) event.getDamager());
            if (victim.isOnDuty() && (attacker.isOnDuty())) {
                event.setCancelled(true);
                return;
            }
            if (victim.isOnDuty()) {
                victim.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "You were hit by: " + ChatColor.RED + attacker.getPlayer().getName());
                victim.getPlayer().getLocation().getWorld().playEffect(victim.getPlayer().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }

        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Guard guard = new Guard(plugin, (Player) event.getEntity());
        if (guard.isOnDuty()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInventoryEvent(InventoryClickEvent event) {
        Guard guard = new Guard(plugin, (Player) event.getWhoClicked());
        if (guard.isOnDuty()) {
            guard.getPlayer().sendMessage(ChatColor.RED + "Please do not edit your inventory while on duty!");
            event.setCancelled(true);
        }
    }

    public void announceGuardDeathMessage(Player guard) {
        List<String> deathmsg = plugin.getConfig().getStringList("guard-death-msg");
        deathmsg = Util.formatMessage(deathmsg, guard);
        for(String msg : deathmsg) {
            plugin.getServer().broadcastMessage(msg);
        }
    }
}
