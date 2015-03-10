package net.xblaze.xBlazeBand;

import java.util.ArrayList;
import java.util.List;

import net.xblaze.xBlazeCore.api.util.InventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagicBandManager {

	public BlazeBand plugin;
	public InventoryManager invman;

	private ItemStack band = new ItemStack(Material.PAPER);

	public MagicBandManager(BlazeBand plugin) {

		this.plugin = plugin;
		this.invman = plugin.invman;
		// Create the MagicBand
		ItemMeta m = this.band.getItemMeta();
		m.setDisplayName(ChatColor.GOLD + "Magicband");
		List<String> itemlore = new ArrayList<String>();
		itemlore.add("Right click to see options.");
		itemlore.add("Can't Be removed.");
		m.setLore(itemlore);
		this.band.setItemMeta(m);
	}

	private ItemStack getBand() {
		return band;
	}

	public boolean isBand(ItemStack i) {
		try {
			if (i.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Magicband")) return true;
		} catch (NullPointerException ignore) {}
		return false;
	}

	public boolean hasBand(Player p) {
		if (p.getInventory().contains(getBand())) return true;
		return false;
	}

	public void openMagicBand(Player p) {
		plugin.menuman.prepare(p);
		p.openInventory(plugin.menuman.MainMenu);
	}

	/*
	 * This puts the Magic into your 8th inventory slot.
	 */
	public void giveMagicBand(Player p, boolean tell) {
		p.getInventory().remove(Material.PAPER);
		invman.replace(p, 8, band);
		if (tell) p.sendMessage(ChatColor.GRAY + "Your MagicBand has been Replaced!");
	}

	public void remMagicBand(Player p) {
		p.getInventory().remove(getBand());
	}

	public void cannotBeMoved(Player p) {
		giveMagicBand(p,false);
		p.sendMessage(ChatColor.GRAY + "Your MagicBand is tightly attached to your wrist.");
	}

}
