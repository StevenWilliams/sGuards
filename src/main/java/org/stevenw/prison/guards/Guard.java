package org.stevenw.prison.guards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.stevenw.prison.guards.util.Util;

import java.util.*;

public class Guard {
    private Player player;
    private ItemStack[] dutyArmour;
    private sGuards plugin;

    private static List<UUID> onDuty = new ArrayList<>();
    private static HashMap<String, ItemStack[]> inventories = new HashMap();
    private static HashMap<String, ItemStack[]> Gamemode = new HashMap();
    private static HashMap<String, ItemStack[]> armour = new HashMap();
    private static HashMap<String, float[]> xp = new HashMap();
    private ItemStack[] armourStack;
    private List<ItemStack> inventory = new ArrayList();

    public Guard(sGuards plugin, Player player) {
        this.player = player;
        this.plugin = plugin;
        Set<String> configArmour = plugin.getConfig().getConfigurationSection("dutyarmour").getKeys(false);


        //Armour
        List<ItemStack> armour = new ArrayList();
        for (String item : configArmour) {
            ItemStack armouritem = Util.loadGuardItem(plugin, "dutyarmour", item, player);
            armouritem.getItemMeta().spigot().setUnbreakable(true);
            armour.add(armouritem);
        }
        this.armourStack = armour.toArray(new ItemStack[armour.size()]);

        //Inventory
        Set<String> configInventory = plugin.getConfig().getConfigurationSection("dutyinv").getKeys(false);
        for (String item : configInventory) {
            inventory.add(Util.loadGuardItem(plugin, "dutyinv", item, player));
        }


    }
    public List<String> getPerms() {
        return plugin.getConfig().getStringList("dutyperms");
    }
    public boolean isOnDuty() {
        if (onDuty.contains(player.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setOnDuty() {
        if (!isOnDuty()) {
            if (onDuty.add(player.getUniqueId())) {
                removePotionEffects();
                savePlayerInventory();
                setGuardInventory();
                setGuardArmour();
                setGuardEffects();
                addPerms();
            }

        }
        return false;
    }

    private void addPerms() {
        for(String perm : getPerms()) {
            plugin.getPerms().playerAdd(player, perm);
        }
    }
    private void removePerms() {
        for(String perm : getPerms()) {
            plugin.getPerms().playerRemove(player, perm);
        }
    }
    private void removePotionEffects() {
        PotionEffect pe;
        for (Iterator localIterator = player.getActivePotionEffects().iterator(); localIterator.hasNext(); player.removePotionEffect(pe.getType())) {
            pe = (PotionEffect) localIterator.next();
        }
    }

    public boolean setOffDuty() {
        if (isOnDuty()) {
            if (onDuty.remove(player.getUniqueId())) {
                removePotionEffects();
                player.getInventory().clear();
                loadPlayerInventory();
                removePerms();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void savePlayerInventory() {
        String pname = player.getName();
        ItemStack[] inv = player.getInventory().getContents();
        ItemStack[] arm = player.getInventory().getArmorContents();
        inventories.put(pname, inv);
        armour.put(pname, arm);
    }

    public void loadPlayerInventory() {
        player.getInventory().setContents((ItemStack[]) inventories.get(player.getName()));
        player.getInventory().setArmorContents((ItemStack[]) armour.get(player.getName()));
        inventories.remove(player.getName());
        armour.remove(player.getName());
    }
    public static List<Player> guardsOnDuty() {
        List<Player> guards = new ArrayList();
        for(UUID guarduuid : onDuty)
        {
            guards.add(Bukkit.getServer().getPlayer(guarduuid));
        }
        return guards;
    }


    public boolean setGuardEffects() {
        Set<String> dutyEffects = plugin.getConfig().getConfigurationSection("dutyeffects").getKeys(false);
        for (String dutyEffect : dutyEffects) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(dutyEffect), 2147483647, plugin.getConfig().getInt("dutyeffects." + dutyEffect)));
        }
        return true;
    }

    public boolean setGuardInventory() {
        player.getInventory().clear();
        for (ItemStack invitem : inventory) {
            player.getInventory().addItem(invitem);
        }
        return true;
    }

    public boolean setGuardArmour() {
        player.getInventory().setArmorContents((armourStack));
        return true;
    }

    public Player getPlayer() {
        return player;
    }
}
