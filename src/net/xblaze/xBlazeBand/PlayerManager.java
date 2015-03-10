package net.xblaze.xBlazeBand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerManager {

	public BlazeBand plugin;

	public PlayerManager(BlazeBand plugin) {
		this.plugin = plugin;
	}

	public void toggleIsolation(Player p) {
		if(!p.hasMetadata("isolation")) {
			p.setMetadata("isolation", new FixedMetadataValue(plugin, true));
			p.sendMessage(ChatColor.GRAY + "You will no longer see others!");
			for(Player otherplayer : Bukkit.getOnlinePlayers()) {
				if (!otherplayer.hasPermission("xblaze.mb.opaque")) {
					p.hidePlayer(otherplayer);
				}
			}
			return;
		} else {
			p.removeMetadata("isolation", plugin);
			p.sendMessage(ChatColor.GRAY + "You are now able to see others again!");
			for(Player otherplayer : Bukkit.getOnlinePlayers()) {
				if (!otherplayer.hasPermission("xblaze.mb.opaque")) {
					p.showPlayer(otherplayer);
				}
			}
			return;
		}
	}

	public void checkIsolation() {
		for (Player guest : Bukkit.getOnlinePlayers()) {
			if(guest.hasMetadata("isolation")) {
				for (Player otherplayer : Bukkit.getOnlinePlayers()) {
					if (!otherplayer.hasPermission("xblaze.mb.hasvanish")) guest.hidePlayer(otherplayer);
				}
			} else {
				for (Player otherplayer : Bukkit.getOnlinePlayers()) {
					if (!otherplayer.hasPermission("xblaze.mb.hasvanish")) guest.showPlayer(otherplayer);
				}
			}
		}
	}
	
    public static Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }

}
