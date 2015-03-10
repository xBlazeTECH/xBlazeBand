package net.xblaze.xBlazeBand;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.xblaze.xBlazeCore.api.nms.NmsManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandInterpreter implements Listener {
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	private NmsManager nmsman;
	private BlazeBand plugin;
	private MagicBandManager mbman;

	public CommandInterpreter(BlazeBand pl) {
		this.plugin = pl;
		this.nmsman = pl.nmsman;
		this.mbman = pl.mbman;
	}

	@EventHandler
	public void onCommandExecuted(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		String[] args = msg.split(" ");
		String cmd = args[0];
		if (cmd.equalsIgnoreCase("/mb")) {
			e.setCancelled(true);
			mbman.giveMagicBand(p,true);
			return;
		}
		if (cmd.equalsIgnoreCase("/fetchtime")) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.AQUA + sdf.format(Calendar.getInstance().getTime()));
		}
		if (cmd.equalsIgnoreCase("/room")) {
			e.setCancelled(true);
			plugin.hotelman.roomCommand(p, args);
		}
		if (cmd.equalsIgnoreCase("/xblaze") || cmd.equalsIgnoreCase("/blaze") || cmd.equalsIgnoreCase("/xblazetech") || cmd.equalsIgnoreCase("/blazetech") || cmd.equalsIgnoreCase("/hotels") || cmd.equalsIgnoreCase("/magicband")) {
			p.sendMessage(ChatColor.GRAY + "Greetings " + p.getName() + ",");
			nmsman.newFancyMessage("Hello, my name is Lansing and I made the Magicband plugin used on this server.")
				.color(ChatColor.GRAY)
			.then("If you need to contact me for any reason, please just: ")
				.color(ChatColor.GRAY)
			.then("Click Here")
				.color(ChatColor.BLUE)
				.style(ChatColor.BOLD , ChatColor.UNDERLINE)
				.link("http://xblaze.net")
			.then(" to visit my website. I hope you enjoy my plugins!")
				.color(ChatColor.GRAY)
			.send(p);
			p.sendMessage(ChatColor.GRAY + "Best Regards,");
			p.sendMessage(ChatColor.GRAY + "Lansing (Private Plugin Contractor)");

			e.setCancelled(true);
			return;
		}
		if (cmd.equalsIgnoreCase("/easteregg")) {
			p.sendMessage(ChatColor.DARK_PURPLE + "You found me! Please enjoy the park, and have a magical day.");
			e.setCancelled(true);
		}
	}
}
