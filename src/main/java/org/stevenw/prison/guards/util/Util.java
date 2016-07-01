package org.stevenw.prison.guards.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stevenw.prison.guards.sGuards;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Util {

    public static ItemStack loadGuardItem(sGuards plugin, String itempath, String item, Player guard) {

        ItemStack loadedStack;

        Integer itemquantity = plugin.getConfig().getInt(itempath + "." + item + "." + "quantity");

        //check if datavalues set
        short datavalueshort;
        if (plugin.getConfig().getString(itempath + "." + item + "." + "datavalue") != null) {
            String datavalue = plugin.getConfig().getString(itempath + "." + item + "." + "datavalue");
            datavalueshort = Short.parseShort(datavalue);
        } else {
            datavalueshort = 0;
        }

        loadedStack = new ItemStack(Material.matchMaterial(item), itemquantity, (short) datavalueshort);

        ItemMeta loadedStackMeta = loadedStack.getItemMeta();
        loadedStackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(itempath + "." + item + ".displayName")));

        List<String> loreItemConfig = plugin.getConfig().getStringList(itempath + "." + item + ".lore");
        List<String> lore = new ArrayList();
        for (String loreItemConfigLine : loreItemConfig) {
            lore.add(formatMessage(loreItemConfigLine, guard));
        }
        loadedStackMeta.setLore(lore);

        if (plugin.getConfig().getConfigurationSection(itempath + "." + item + ".enchantments") != null) {
            Set<String> enchantItemConfig = plugin.getConfig().getConfigurationSection(itempath + "." + item + ".enchantments").getKeys(false);
            for (String enchantment : enchantItemConfig) {
                loadedStackMeta.addEnchant(Enchantment.getByName(enchantment), plugin.getConfig().getInt(itempath + "." + item + ".enchantments." + enchantment), true);
            }
        }

        loadedStack.setItemMeta(loadedStackMeta);
        return loadedStack;
    }

    public static String formatMessage(String msg, Player guard) {
        return ChatColor.translateAlternateColorCodes('&', msg.replace("{GUARD}", guard.getName()));
    }

    public static List<String> formatMessage(List<String> msglist, Player guard) {
        for (int i = 0; i < msglist.size(); i++) {
            msglist.set(i, formatMessage(msglist.get(i), guard));
        }
        return msglist;
    }

}
