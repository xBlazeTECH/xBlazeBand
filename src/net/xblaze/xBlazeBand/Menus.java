package net.xblaze.xBlazeBand;

import java.util.ArrayList;
import java.util.HashMap;

import net.xblaze.xBlazeCore.api.util.ItemManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class Menus {

	private BlazeBand plugin;
	private ItemManager itemman;
	private PlayerManager pman;
	
	public Inventory MainMenu = Bukkit.createInventory(null, 9, "MagicBand - Main Menu");
	public Inventory ServerMenu = Bukkit.createInventory(null, 9, "MagicBand - Server Directory");
	public Inventory ParkMenu = Bukkit.createInventory(null, 9, "MagicBand - Park Transit");
	public Inventory ResortMenu = Bukkit.createInventory(null, 9, "MagicBand - Hotels And Resorts");
	public Inventory FoodMenu = Bukkit.createInventory(null, 9, "MagicBand - Food Menu");
	
	public HashMap<Player,Inventory> PreviousInventory = new HashMap<Player,Inventory>();
	/* TODO: 
	 * Add player metadata that includes "Last Menu" and set it to Main Menu
	 * when you first open the magic band, then as each subsequent menu is opened,
	 * set it to the previous menu.
	 * 
	 * Make an iteratable array of menus and add each opened menu to the array
	 * as they are opened so that you can go to the previous menu very easily
	 * 
	 */

	public Menus(BlazeBand pl) {
		this.plugin = pl;
		this.itemman = pl.itemman;
		this.pman = pl.pman;
	}

	public void prepare(Player p) {
		MainMenu.clear();
		createDisplay(Material.COMPASS, MainMenu, 0, "Change Server", "Join a different server.");
		createDisplay(Material.EMPTY_MAP, MainMenu, 1, "Park Transit", "Travel Around the Parks.");
		if (plugin.getServer().getPluginManager().isPluginEnabled("xBlazeSign")) createDisplay(Material.BOOK, MainMenu, 3, "Autographs", "Learn more about signing.");
		//createDisplay(Material.APPLE, MainMenu, 4, "Find Food", "Get a bite to eat.");
		createDisplay(Material.BED, MainMenu, 5, "Hotels & Resorts", "Rent a Room.");
		createDisplay(Material.ENDER_CHEST, MainMenu, 6, "Magic Locker", "Open your Personal Locker.");
		createDisplay(Material.NETHER_STAR, MainMenu, 7, "Show/Hide Players", "Toggles seeing other players.");
		createDisplay(Material.GOLD_NUGGET, MainMenu, 8, "Bank Balance", "$" + Double.toString(BlazeBand.econ.getBalance(p.getName())));

		/* Obselete */
		FoodMenu.clear();
		createDisplay(Material.PAPER, FoodMenu, 0, "Back", "Back to Main Menu");
		createDisplay(Material.COOKED_BEEF, FoodMenu, 4, "Casey's", "/warp caseys");
		createDisplay(Material.NETHER_STAR, FoodMenu, 5, "Starlight", "/warp starlight");
		createDisplay(Material.CAKE, FoodMenu, 6, "Coffee", "/warp coffee");
		createDisplay(Material.MELON, FoodMenu, 7, "Seasons", "/warp seasons");
		createDisplay(Material.POTATO_ITEM, FoodMenu, 8, "Gerty", "/warp gerty");

		
		/* Obselete */
		ParkMenu.clear();
		createDisplay(Material.PAPER, ParkMenu, 0, "Back", "Back to Main Menu");
		createDisplay(Material.DIAMOND_HOE, ParkMenu, 3, "Magic Kingdom", "/warp mk");
		createDisplay(Material.DIAMOND_BARDING, ParkMenu, 4, "Animal Kingdom", "/warp ak");
		createDisplay(Material.JUKEBOX, ParkMenu, 5, "Hollywood Studios", "/warp hws");
		createDisplay(Material.SNOW_BALL, ParkMenu, 6, "Epcot", "/warp epcot");
		createDisplay(Material.BOAT, ParkMenu, 7, "Port", "/warp port");
		createDisplay(Material.WATER, ParkMenu, 8, "Typhoon", "/warp typhoon");

		ServerMenu.clear();
		createDisplay(Material.PAPER, ServerMenu, 0, "Back", "Back to Main Menu");
		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 2, "§aWalt Disney World", "Visit Walt Disney World");
		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 3, "§aDisneyland California", "Take a trip to Disneyland");
		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 4, "§aUniversal Studios Orlando", "Click to join!");
//		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 5, "§aDisney Cruise Line", "Join your friends on Disney Cruise Line.");
		createDisplay(new Wool(DyeColor.YELLOW).toItemStack(), ServerMenu, 5, "§aDisney Cruise Line", "Imagineering in progress...");
		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 6, "§aMcParks Creative", "Build your Dreams.");
		if (p.hasPermission("xblaze.mb.server.custom")) createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 7, "§aCustom", "I am not sure what this is (xBlaze)");
		if (!p.hasPermission("xblaze.mb.server.custom")) createDisplay(new Wool(DyeColor.RED).toItemStack(), ServerMenu, 7, "§aCustom", "Sorry, Cast Members Only");
		createDisplay(new Wool(DyeColor.RED).toItemStack(), ServerMenu, 8, "§aCustom", "Sorry, Cast Members Only");
//		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 7, "§aMcParks Main!", "Click to join!");
//		createDisplay(new Wool(DyeColor.LIME).toItemStack(), ServerMenu, 8, "§aMcParks Creative!", "Click to join!");
	}
	 
	public static void createDisplay(ItemStack item, Inventory inv, int Slot, String name, String lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		inv.setItem(Slot, item); 
	}
	public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
		createDisplay(new ItemStack(material), inv, Slot, name, lore);
	}

	public void openResorts(Player p) {
		ResortMenu.clear();
		createDisplay(Material.PAPER, ResortMenu, 0, "Back", "Back to Main Menu");
		createDisplay(itemman.getSpawnEgg(EntityType.GHAST), ResortMenu, 5, "Contemporary", "Click to Warp");
		createDisplay(itemman.getSpawnEgg(EntityType.CHICKEN), ResortMenu, 6, "Floridian", "Click to Warp");
		createDisplay(itemman.getSpawnEgg(EntityType.GHAST), ResortMenu, 7, "Polynesian", "Click to Warp");
		createDisplay(itemman.getSpawnEgg(EntityType.VILLAGER), ResortMenu, 8, "Disney Dream", "Click to Warp");
		p.openInventory(ResortMenu);
	}

	public void display(Player p, Inventory menu) {
		p.openInventory(menu);
	}

	public void clicked(Player p, MenuType menu, InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		int slot = event.getSlot();
		ClickType click = event.getClick();
		switch (menu) {
		case MAINMENU:
			if (slot == 0) {
				p.openInventory(this.ServerMenu);
				return;
			}
			if (slot == 1) {
				p.openInventory(this.ParkMenu);
				return;
			}
			if (slot == 2) {
				break;
			}
			if (slot == 3) {
				p.chat("/signing");
				break;
			}
			if (slot == 4) {
				p.openInventory(this.FoodMenu);
				return;
			}
			if (slot == 5) {
				openResorts(p);
				return;
			}
			if (slot == 6) {
				p.openInventory(p.getEnderChest());
				return;
			}
			if (slot == 7) {
				pman.toggleIsolation(p);
				break;
			}
			if (slot == 8) {
				return;
			}
		case SERVERMENU:
			if (slot == 0) {
				this.display(p, this.MainMenu);
				return;
			}
			if (slot == 1) {
				break;
			}
			if (slot == 2) {
				plugin.sendPlayerToServer(p, "WDW");
				break;
			}
			if (slot == 3) {
				plugin.sendPlayerToServer(p, "DL");
				break;
			}
			if (slot == 4) {
				plugin.sendPlayerToServer(p, "USO");
				break;
			}
			if (slot == 5) {
				plugin.sendPlayerToServer(p, "DCL");
				break;
			}
			if (slot == 6) {
				plugin.sendPlayerToServer(p, "Creative");
				break;
			}
			if (slot == 7) {
				if (p.hasPermission("xblaze.mb.server.custom")) plugin.bungeemanager.sendPlayerToServer(p, "main");
				break;
			}
			if (slot == 8) {
				break;
			}
		case PARKMENU:
			if (slot == 0) {
				this.display(p, this.MainMenu);
				return;
			}
			if (slot == 1) {
				break;
			}
			if (slot == 2) {
				break;
			}
			if (slot == 3) {
				p.chat("/warp mk");
				break;
			}
			if (slot == 4) {
				p.chat("/warp ak");
				break;
			}
			if (slot == 5) {
				p.chat("/warp hws");
				break;
			}
			if (slot == 6) {
				p.chat("/warp epcot");
				break;
			}
			if (slot == 7) {
				p.chat("/warp port");
				break;
			}
			if (slot == 8) {
				p.chat("/warp typhoon");
				break;
			}
		case RESORTMENU:
			if (slot == 0) {
				this.display(p, this.MainMenu);
				return;
			}
			if (slot < 5) return;
			if (click.isShiftClick()) {
				if (slot == 5) {
					p.chat("/warp cr");
					break;
				}
				if (slot == 6) {
					p.chat("/warp gf");
					break;
				}
				if (slot == 7) {
					p.chat("/warp poly");
					break;
				}
				if (slot == 8) {
					p.chat("/warp port");
					break;
				}
			} else {
				if (slot == 5) {
					p.sendMessage(ChatColor.RED + "This hotel is not currently in operation!");
					p.chat("/warp cr");
					break;
				}
				if (slot == 6) {
					p.openInventory(plugin.hotelman.floors("gf", p));
					return;
				}
				if (slot == 7) {
					p.sendMessage(ChatColor.RED + "This hotel is not currently in operation!");
					p.chat("/warp poly");
					break;
				}
				if (slot == 8) {
					p.sendMessage(ChatColor.RED + "This hotel is not currently in operation!");
					p.chat("/warp port");
					break;
				}
			}
			
		case FOODMENU:
			if (slot == 0) {
				this.display(p, this.MainMenu);
				return;
			}
			if (slot == 1) {
				return;
			}
			if (slot == 2) {
				return;
			}
			if (slot == 3) {
				return;
			}
			if (slot == 4) {
				p.chat("/warp caseys");
				break;
			}
			if (slot == 5) {
				p.chat("/warp starlight");
				break;
			}
			if (slot == 6) {
				p.chat("/warp coffee");
				break;
			}
			if (slot == 7) {
				p.chat("/warp seasons");
				break;
			}
			if (slot == 8) {
				p.chat("/warp gerty");
				break;
			}
			break;
		case FLOORSMENU:
			if (slot == 0) {
				this.display(p, this.ResortMenu);
				return;
			}
			if (slot == 1) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 1));
				return;
			}
			if (slot == 2) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 2));
				return;
			}
			if (slot == 3) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 3));
				return;
			}
			if (slot == 4) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 4));
				return;
			}
			if (slot == 5) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 5));
				return;
			}
			if (slot == 6) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 6));
				return;
			}
			if (slot == 7) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 7));
				return;
			}
			if (slot == 8) {
				p.openInventory(plugin.hotelman.getFloor(p, "gf", 8));
				return;
			}
			break;
		case ROOMSMENU:
			if (slot == 0) {
				display(p, plugin.hotelman.floors("gf", p));
				return;
			}
//			if (slot == 45) {
//				p.sendMessage(ChatColor.GOLD + "Remove me: Switching to Previous Page!");
//				return;
//			}
//			if (slot == 53) {
//				p.sendMessage(ChatColor.GOLD + "Remove me: Switching to Next Page!");
//				return;
//			}
			String roomnumber = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("Room #", "");
			p.sendMessage("You selected Room: " + roomnumber);
			plugin.hotelman.teleportToRoom(p, "gf", roomnumber);
		default:
			break;
		}
		p.closeInventory();
	}
	
	public void setPreviousMenu(Player p, Inventory inv) {
		this.PreviousInventory.put(p, inv);
	}
	public Inventory getPreviousInventory(Player p) {
		return this.PreviousInventory.get(p);
	}

}
