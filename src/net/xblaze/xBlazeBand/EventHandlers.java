package net.xblaze.xBlazeBand;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class EventHandlers implements Listener {

	// Local Defintions
	private HotelManager hotel;
	private MagicBandManager mbman;
	private BlazeBand plugin;
	
	// Constructor to Capture Plugin.
	public EventHandlers(BlazeBand pl) {
		
		this.plugin = pl;
		// Capture the Local Definitions
		this.hotel = pl.hotelman;
		this.mbman = pl.mbman;
	}

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getNewGameMode().equals(GameMode.SURVIVAL)) {
			p.sendMessage(ChatColor.GRAY + "You are now in survival and are required to have a magicband. Here you go! :)");
			mbman.giveMagicBand(p,false);
		}
		if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
			p.sendMessage(ChatColor.GRAY + "Psst, you may now remove your magicband, by clicking on it in your inventory.");
			mbman.giveMagicBand(p,false);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (mbman.isBand(event.getCurrentItem())) {
			if (event.getClick().isCreativeAction()) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.GREEN + "Your magicband turns into a pretty " + ChatColor.LIGHT_PURPLE + "pink" + ChatColor.GREEN + " butterfly and flys away.");
				mbman.remMagicBand(p);
				return;
			} else {
				mbman.cannotBeMoved(p);
			}
		}
		if (event.getInventory().equals(p.getInventory())) return;
		if (event.getInventory().getTitle().equals(plugin.menuman.ResortMenu.getTitle())) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.RESORTMENU, event);
		}
		if (event.getInventory().getTitle().equals(plugin.menuman.FoodMenu.getTitle())) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.FOODMENU, event);
		}
		if (event.getInventory().getName().equals(plugin.menuman.ParkMenu.getName())) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.PARKMENU, event);
		}

		if (event.getInventory().getTitle().equals(plugin.menuman.MainMenu.getTitle())) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.MAINMENU, event);
		}
		if (event.getInventory().getTitle().equals(plugin.menuman.ServerMenu.getTitle())) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.SERVERMENU, event);
		}
		if (event.getInventory().getTitle().equals("McMagic - Floor Directory")) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.FLOORSMENU, event);
		}
		if (event.getInventory().getTitle().equals("McMagic - Room Directory")) {
			event.setCancelled(true);
			plugin.menuman.clicked(p, MenuType.ROOMSMENU, event);
		}
	}

	@EventHandler
	public void onItemDropped(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if (mbman.isBand(event.getItemDrop().getItemStack())) {
			event.getItemDrop().remove();
			mbman.cannotBeMoved(p);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		for (ItemStack i : event.getDrops()) {
			if (mbman.isBand(i)) i.setType(Material.AIR);
		}
	}

	@EventHandler
	public void onItemUsed(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		try {
			if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && !event.getClickedBlock().getType().equals(Material.WOODEN_DOOR)) {
				if (event.getMaterial() == Material.PAPER) {
					event.setCancelled(true);
					mbman.openMagicBand(p);
					return;
				}
			}
		} catch (NullPointerException err) {
			if (event.getMaterial() == Material.PAPER) {
				event.setCancelled(true);
				mbman.openMagicBand(p);
				return;
			}
		}
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType().equals(Material.WOODEN_DOOR)) {
				hotel.doorInteract(event);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		mbman.giveMagicBand(p,false);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if ((!p.hasPermission("xblaze.mb.ignorechat")) &&(event.getMessage().matches(".*magic.*band.*work.*") || event.getMessage().matches(".*magicband.*work.*") || event.getMessage().matches(".*magicband.*broken.*") || event.getMessage().matches(".*magic.*band.*broken.*"))) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.GREEN + "We are sorry for the mishap. " + ChatColor.GOLD + "MagicBand" + ChatColor.GREEN + " replaced!");
			mbman.giveMagicBand(p,true);
		}
	}

}
