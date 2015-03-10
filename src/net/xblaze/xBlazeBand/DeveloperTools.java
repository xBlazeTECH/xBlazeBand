package net.xblaze.xBlazeBand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class DeveloperTools {
	public BlazeBand plugin;
	
	public DeveloperTools(BlazeBand plugin) {
		this.plugin = plugin;
	}
	
	public void addDebugger(Player p) {
		p.setMetadata("xblaze-dev", new FixedMetadataValue(plugin,true));
	}
	
	public void removeDebugger(Player p) {
		p.removeMetadata("xblaze-dev", plugin);
	}
	
	public boolean isDebugging(Player p) {
		return p.hasMetadata("xblaze-dev");
	}
	
	public void sendDebugMessage(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (isDebugging(p)) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "xBlaze" + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_RED + "Debug: " + message);
			}
		}
	}
}
