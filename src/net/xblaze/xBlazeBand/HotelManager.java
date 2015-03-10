package net.xblaze.xBlazeBand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.xblaze.xBlazeCore.api.util.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;

public class HotelManager {
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private BlazeBand plugin;
	private FileConfiguration config;
	private ItemManager itemman;
	public HotelManager(BlazeBand pl) {
		this.plugin = pl;
		this.config = pl.getConfig();
		this.itemman = pl.itemman;
	}

	public Inventory rooms(String hotel, Player p) {
		Inventory Directory = Bukkit.createInventory(null, 54, "Magicband - Room Directory");
		int currslot = 1;
		createDisplay(Material.PAPER, Directory, 0, "Back", "Back to Main Menu");
		createDisplay(Material.QUARTZ_ORE, Directory, 45, "Previous Page", "Go to the previous page.");
		createDisplay(Material.QUARTZ_ORE, Directory, 53, "Next Page", "Go to the next page.");
		for (int room = 0; room <= plugin.getConfig().getInt("gf-max") ; room++) {
			try {
				String prefix = "rm." +  hotel + ".";
				if (plugin.getConfig().contains(prefix + room + ".doorx") && plugin.getConfig().contains(prefix + room + ".doory") && plugin.getConfig().contains(prefix + room + ".doorz")) {
					ItemStack block = new ItemStack(Material.WOOL);
					ArrayList<String> lore = new ArrayList<String>();
					Wool wool = (Wool) block.getData();
					if (plugin.getConfig().contains(prefix + room + ".renter")) {
						if (config.getString(prefix + room + ".renter").equals(p.getName())) {
							wool.setColor(DyeColor.BLUE);
							Date now = Calendar.getInstance().getTime();
							Date then = null;
							try {
								then = sdf.parse((String) plugin.getConfig().get(prefix+room+".datestamp"));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							lore.add(getDateDiff(now, then, TimeUnit.MINUTES) + " mins. left");
						} else {
							wool.setColor(DyeColor.RED);
							lore.add("Occupied");
						}
					} else {
						wool.setColor(DyeColor.LIME);
						lore.add("$" + plugin.getConfig().getString(prefix + room + ".cost")+ ".00");
					}
					createDisplay(wool.toItemStack(),Directory,currslot,"Room #" + room, lore);

					currslot++;
				}
			} catch(NullPointerException ignore) {}
		}
		return Directory;
	}

	public Inventory floors(String hotel, Player p) {
		Inventory Directory = Bukkit.createInventory(null, 9, "McMagic - Floor Directory");
		createDisplay(Material.PAPER, Directory, 0, "Back", "Back to Main Menu");
		createDisplay(itemman.getWool(DyeColor.RED), Directory, 1, "100's", "Rooms 100-199");
		createDisplay(itemman.getWool(DyeColor.ORANGE), Directory, 2, "200's", "Rooms 200-299");
		createDisplay(itemman.getWool(DyeColor.YELLOW), Directory, 3, "300's", "Rooms 300-399");
		createDisplay(itemman.getWool(DyeColor.LIME), Directory, 4, "400's", "Rooms 400-499");
		createDisplay(itemman.getWool(DyeColor.GREEN), Directory, 5, "500's", "Rooms 500-599");
		createDisplay(itemman.getWool(DyeColor.CYAN), Directory, 6, "600's", "Rooms 600-699");
		createDisplay(itemman.getWool(DyeColor.BLUE), Directory, 7, "700's", "Rooms 700-799");
		createDisplay(itemman.getWool(DyeColor.PURPLE), Directory, 8, "800's", "Rooms 800-899");
		return Directory;
	}

	public Inventory getFloor(Player p, String hotel, int floor) {
		Inventory Directory = Bukkit.createInventory(null, 54, "McMagic - Room Directory");
		int currslot = 1;
		createDisplay(Material.PAPER, Directory, 0, "Back", "Back to Main Menu");
		int range = floor*100;

		for (int room = 0; room <= plugin.getConfig().getInt("gf-max") ; room++) {
			try {
				String prefix = "rm." +  hotel + ".";
				if (plugin.getConfig().contains(prefix + room + ".doorx") && plugin.getConfig().contains(prefix + room + ".doory") && plugin.getConfig().contains(prefix + room + ".doorz")) {
					if (room > range && room < range+99) {
						ItemStack block = new ItemStack(Material.WOOL);
						ArrayList<String> lore = new ArrayList<String>();
						Wool wool = (Wool) block.getData();
						if (plugin.getConfig().contains(prefix + room + ".renter")) {
							if (config.getString(prefix + room + ".renter").equals(p.getName())) {
								wool.setColor(DyeColor.BLUE);
								Date now = Calendar.getInstance().getTime();
								Date then = null;
								try {
									then = sdf.parse((String) plugin.getConfig().get(prefix+room+".datestamp"));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								lore.add(ChatColor.BLUE + "Rented by You!");
								lore.add(ChatColor.LIGHT_PURPLE + "Minutes Remaining:");
								lore.add(ChatColor.AQUA + "" + getDateDiff(now, then, TimeUnit.MINUTES));
							} else {
								wool.setColor(DyeColor.RED);
								lore.add("Occupied");
							}
						} else {
							wool.setColor(DyeColor.LIME);
							lore.add("$" + plugin.getConfig().getString(prefix + room + ".cost")+ ".00");
						}
						createDisplay(wool.toItemStack(),Directory,currslot,"Room #" + room, lore);

						currslot++;
					}
				}
			} catch(NullPointerException ignore) {}
		}
		return Directory;
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
	
	public static void createDisplay(ItemStack item, Inventory inv, int Slot, String name, ArrayList<String> Lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		inv.setItem(Slot, item); 
	}

	public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
		createDisplay(new ItemStack(material), inv, Slot, name, lore);
	}

	public void doorInteract(PlayerInteractEvent e) {
		plugin.saveConfig();
		Player p = e.getPlayer();
		if (p.hasMetadata("xblaze-rap") || p.hasMetadata("xblaze-rar")) {
			e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "!!! " + ChatColor.GREEN + "Creating Door Protection." + ChatColor.DARK_GREEN + " !!!" );

			if (p.hasMetadata("xblaze-rap")) p.removeMetadata("xblaze-rap", plugin);
			e.setCancelled(true);
			String hotel = p.getMetadata("hotel").get(0).asString();
			String room = p.getMetadata("room").get(0).asString();
			int roomnumber = p.getMetadata("room").get(0).asInt();
			int cost = p.getMetadata("cost").get(0).asInt();

			int xint = e.getClickedBlock().getX();
			int yint = e.getClickedBlock().getY();
			int zint = e.getClickedBlock().getZ();

			p.sendMessage(ChatColor.AQUA + "Creating New Room: " + ChatColor.GRAY + hotel + " " + room + " " + cost);

			// Check to make sure that we get the coordinates of the BOTTOM of the door.
			Location under = new Location(e.getPlayer().getWorld(), xint, yint-1, zint);
			if (under.getBlock().getType().equals(Material.WOODEN_DOOR)) {
				yint = yint - 1;
			}

			String prefix = "rm." +  hotel + "." + room;

			if (config.contains(prefix)) {
				p.sendMessage(ChatColor.GREEN + "Room Already Found, Redefining...");
			} else {
				p.sendMessage(ChatColor.GRAY + "Room Not found, Creating new Room...");
				int last = config.getInt("gf-greatest");
				//				p.sendMessage(ChatColor.GRAY + "Highest Room Number in Hotel: " + last);
				//				p.sendMessage(ChatColor.GRAY + "Current Room Number Selected:" + roomnumber);
				if (last < roomnumber) {
					p.sendMessage(ChatColor.GRAY + "Detected that Selected room is now the Higest!");
					//					p.sendMessage(ChatColor.GRAY + "Now updating the highest number in config.");
					config.set("gf-max", roomnumber);
					p.sendMessage(ChatColor.GRAY + "Changed " + last + " to " + roomnumber + " in config.");
				}
			}

			p.sendMessage(ChatColor.GRAY + "Storing room in Configuration File.");
			config.set(prefix + ".doorx", xint);
			config.set(prefix + ".doory", yint);
			config.set(prefix + ".doorz", zint);

			config.set(prefix + ".warpx", p.getLocation().getX());
			config.set(prefix + ".warpy", p.getLocation().getY());
			config.set(prefix + ".warpz", p.getLocation().getZ());
			config.set(prefix + ".warppitch", 0);
			config.set(prefix + ".warpyaw", Math.round(p.getLocation().getYaw()));

			config.set(prefix + ".cost", cost);
			p.sendMessage(ChatColor.GREEN + "Stored Sucessfully.");

			int next = 0;
			if (p.hasMetadata("xblaze-rar")) next = roomnumber + 1;
			if (p.hasMetadata("xblaze-rar")) p.setMetadata("room", new FixedMetadataValue(plugin, next));
			if (p.hasMetadata("xblaze-rar")) p.sendMessage(ChatColor.GREEN + "Please click the door for room number: " + next);

			plugin.saveConfig();

			/* This was removed because people can just redefine the room.
		} else if (p.hasMetadata("RoomSetWarp")){
			p.sendMessage(ChatColor.GRAY + "Player is ready to set warp!");
			int xint = e.getClickedBlock().getX();
			int yint = e.getClickedBlock().getY();
			int zint = e.getClickedBlock().getZ();

			// Check to make sure that we get the coordinates of the BOTTOM of the door.
			Location under = new Location(e.getPlayer().getWorld(), xint, yint-1, zint);
			if (under.getBlock().getType().equals(Material.WOODEN_DOOR)) yint = yint - 1;

			Location blockLocation = new Location(p.getWorld(), xint, yint, zint);

			p.sendMessage(ChatColor.GRAY + "You clicked: " + xint + " " + yint + " " + zint);
			try {
				p.sendMessage(ChatColor.GRAY + "Iterating for: " + config.getInt("gf-max"));
				String hotel = "gf";
				String prefix = "rm." + hotel + ".";
				p.sendMessage(ChatColor.GRAY + "Iteration Started!");
				for (int room = 0; room <= config.getInt("gf-max"); room++) {
					if (config.contains(prefix + room + ".doorx") && config.contains(prefix + room + ".doory") && config.contains(prefix + room + ".doorz")) {
						p.sendMessage(ChatColor.GRAY + "We found room: " + room);
						int tmpx = config.getInt(prefix + room + ".doorx");
						int tmpy = config.getInt(prefix + room + ".doory");
						int tmpz = config.getInt(prefix + room + ".doorz");
						Location tmploc = new Location(p.getWorld(), tmpx, tmpy, tmpz);
						if (blockLocation.equals(tmploc)) {
							p.sendMessage(ChatColor.GRAY + "We have found a MATCHING room: " + room);
							config.set(prefix + ".warpx", p.getLocation().getX());
							config.set(prefix + ".warpy", p.getLocation().getY());
							config.set(prefix + ".warpz", p.getLocation().getZ());
							config.set(prefix + ".warppitch", p.getLocation().getPitch());
							config.set(prefix + ".warpyaw", p.getLocation().getYaw());
						}
					}
				}
			} catch (NullPointerException err) {
				p.sendMessage(ChatColor.RED + "Not Found!");
			}
			 */
		} else {
			int xint = e.getClickedBlock().getX();
			int yint = e.getClickedBlock().getY();
			int zint = e.getClickedBlock().getZ();

			// Check to make sure that we get the coordinates of the BOTTOM of the door.
			Location under = new Location(e.getPlayer().getWorld(), xint, yint-1, zint);
			if (under.getBlock().getType().equals(Material.WOODEN_DOOR)) yint = yint - 1;

			Location blockLocation = new Location(p.getWorld(), xint, yint, zint);

			//			p.sendMessage(ChatColor.GRAY + "You clicked: " + xint + " " + yint + " " + zint);
			try {
				//				p.sendMessage(ChatColor.GRAY + "Iterating for: " + config.getInt("gf-max"));

				String hotel = "gf";
				String prefix = "rm." + hotel + ".";
				//				p.sendMessage(ChatColor.GRAY + "Iteration Started!");
				for (int room = 0; room <= config.getInt("gf-max"); room++) {
					if (config.contains(prefix + room + ".doorx") && config.contains(prefix + room + ".doory") && config.contains(prefix + room + ".doorz")) {
						//						p.sendMessage(ChatColor.GRAY + "We found room: " + room);
						int tmpx = config.getInt(prefix + room + ".doorx");
						int tmpy = config.getInt(prefix + room + ".doory");
						int tmpz = config.getInt(prefix + room + ".doorz");
						Location tmploc = new Location(p.getWorld(), tmpx, tmpy, tmpz);
						if (blockLocation.equals(tmploc)) {
							if (config.contains(prefix + room + ".renter")) {
								if (config.getString(prefix + room + ".renter").equalsIgnoreCase(p.getName())) {
									return;
								} else {
									p.sendMessage(ChatColor.GREEN + "You have knocked on " + config.getString(prefix + room + ".renter") + "'s door.");
									this.slamDoor(blockLocation);
									e.setCancelled(true);
									return;
								}
							} else {
								try {
									if (p.getMetadata("prebuy-hotel").get(0).asString().trim().equalsIgnoreCase(hotel.trim()) && p.getMetadata("prebuy-room").get(0).asString().trim().equalsIgnoreCase(String.valueOf(room).trim())) {
										if (plugin.mbman.isBand(p.getItemInHand())) {
											if (BlazeBand.econ.withdrawPlayer(p.getName(), config.getInt(prefix + room + ".cost")).transactionSuccess()) {
												
												Calendar moment = Calendar.getInstance();
												Date now = moment.getTime();
												moment.add(Calendar.WEEK_OF_YEAR, 1);
												Date then = moment.getTime();
																								
												// Simple Date Formatting: MM-dd-yyyy hh:mm:ss
												config.set(prefix + room + ".datestamp", sdf.format(then));
												plugin.saveConfig();
												
												// Save the Configuration												
												p.sendMessage(ChatColor.GREEN + "Current Time and Date " + sdf.format(now));

												p.sendMessage(ChatColor.GREEN + "Expiration Time and Date: " + sdf.format(then));
												p.sendMessage(ChatColor.GRAY + "(If you ever want the server time just do /fetchtime");

												config.set(prefix + room + ".renter", p.getName());
												p.sendMessage(ChatColor.GREEN + "You have purchased this room for $" + config.getInt(prefix + room + ".cost"));
												p.sendMessage(ChatColor.AQUA + "(This room expires in " + getDateDiff(now, then, TimeUnit.MINUTES) + " minutes)");
												p.removeMetadata("prebuy-hotel", plugin);
												p.removeMetadata("prebuy-room", plugin);
												this.slamDoor(blockLocation);
												plugin.saveConfig();
												e.setCancelled(true);
												return;
											} else {
												p.sendMessage(ChatColor.RED + "You do not have $" + config.getInt(prefix + room + ".cost") + " to pay for the room. :(");
												p.sendMessage(ChatColor.GREEN + "Mickey Mouse suggests that you /warp parkour to get some money first! ;)");
												this.slamDoor(blockLocation);
												e.setCancelled(true);
												return;
											}
										} else {
											p.sendMessage(ChatColor.RED + "You must be holding your magic band to rent rooms in this hotel!");
											this.slamDoor(blockLocation);
											e.setCancelled(true);
											return;
										}
									} else {
										p.setMetadata("prebuy-hotel", new FixedMetadataValue(plugin, hotel));
										p.setMetadata("prebuy-room", new FixedMetadataValue(plugin, room));
										p.sendMessage(ChatColor.AQUA + "This room costs $" + config.getInt(prefix + room + ".cost"));
										p.sendMessage(ChatColor.AQUA + "If you wish to purchase it, please click it with your magic band!");
										this.slamDoor(blockLocation);
										e.setCancelled(true);
										return;
									}
								} catch (IndexOutOfBoundsException err1) {
									p.setMetadata("prebuy-hotel", new FixedMetadataValue(plugin, hotel));
									p.setMetadata("prebuy-room", new FixedMetadataValue(plugin, room));
									p.sendMessage(ChatColor.AQUA + "This room costs $" + config.getInt(prefix + room + ".cost"));
									p.sendMessage(ChatColor.AQUA + "If you wish to purchase it, please click it with your magic band!");
									this.slamDoor(blockLocation);
									e.setCancelled(true);
									return;
								}
							}
						}
					}
				}
			} catch (NullPointerException err) {
				p.sendMessage(ChatColor.RED + "Not Found!");
				err.printStackTrace();
			}
		}
	}

	public void roomCommand(Player p, String[] args) {
		for (int i = 0; i<args.length; i++) p.sendMessage(ChatColor.RED + "Argument " + i + " = " + args[i]);
		if (p.hasPermission("xblaze.mb.add")) {
			if (args[1].equalsIgnoreCase("add")) {
				//				p.sendMessage(ChatColor.GRAY + "Recieved Add!");
				if (args.length != 5) {
					p.sendMessage(ChatColor.AQUA + "Usage: /room add <gf|poly|port|cont> <roomnumber> <cost>");
				} else {
					//					p.sendMessage(ChatColor.GRAY + "Hotel: " + args[2]);
					p.setMetadata("hotel", new FixedMetadataValue(plugin, args[2]));
					//					p.sendMessage(ChatColor.GRAY + "Room: " + args[3]);
					p.setMetadata("room", new FixedMetadataValue(plugin, args[3]));
					//					p.sendMessage(ChatColor.GRAY + "Cost: " + args[4]);
					p.setMetadata("cost", new FixedMetadataValue(plugin, args[4]));
					//					p.sendMessage(ChatColor.GRAY + "Marking Player As Adding Room");
					p.setMetadata("xblaze-rap", new FixedMetadataValue(plugin, true));
					//					p.sendMessage(ChatColor.GRAY + "Player Marked as Adding Room");
					p.sendMessage(ChatColor.DARK_GREEN + "!!! " + ChatColor.GREEN + "Please click the door to add room" + ChatColor.DARK_GREEN + " !!!");
				}
			} else {
				p.sendMessage(ChatColor.RED + "For more info on this command, please do /room add");
				p.sendMessage(ChatColor.RED + "You DO have permission to use this command, but if you don't know what you are doing, ask for help!!!");
				p.sendMessage(ChatColor.RED + "This can be a very dangerous command if you do not know what you are doing!!!");
			}
		}
		if (p.hasPermission("xblaze.mb.recurse")) {
			if (args[1].equalsIgnoreCase("recurse")) {
				if (args.length != 5) {
					p.sendMessage(ChatColor.AQUA + "Usage: /room recurse <gf|poly|port|cont> <firstroom> <cost>");
					return;
				}
				p.setMetadata("hotel", new FixedMetadataValue(plugin, args[2]));
				p.sendMessage(ChatColor.GRAY + "Room: " + args[3]);
				p.setMetadata("room", new FixedMetadataValue(plugin, args[3]));
				p.sendMessage(ChatColor.GRAY + "Cost: " + args[4]);
				p.setMetadata("cost", new FixedMetadataValue(plugin, args[4]));
				p.setMetadata("xblaze-rar", new FixedMetadataValue(plugin, true));
				p.sendMessage(ChatColor.DARK_GREEN + "!!! " + ChatColor.GREEN + "Please click the door to add room" + ChatColor.DARK_GREEN + " !!!");
			}
			if (args[1].equalsIgnoreCase("uncurse")) {
				p.removeMetadata("xblaze-rar", plugin);
				p.removeMetadata("hotel", plugin);
				p.removeMetadata("room", plugin);
				p.removeMetadata("cost", plugin);
				p.sendMessage(ChatColor.DARK_GREEN + "!!! " + ChatColor.GREEN + "You are no longer adding recursively" + ChatColor.DARK_GREEN + " !!!");
			}
		}
		if (p.hasPermission("xblaze.mb.setwarp")) {
			if (args[1].equalsIgnoreCase("setwarp")) {
				p.sendMessage(ChatColor.GRAY + "Recieved Setwarp!");
				if (args.length != 4) {
					p.sendMessage(ChatColor.AQUA + "Usage: /room setwarp");
				} else {
					p.sendMessage(ChatColor.GRAY + "Hotel: " + args[2]);
					p.setMetadata("hotel", new FixedMetadataValue(plugin, args[2]));
					p.sendMessage(ChatColor.GRAY + "Room: " + args[3]);
					p.setMetadata("room", new FixedMetadataValue(plugin, args[3]));
					p.sendMessage(ChatColor.GRAY + "Cost: " + args[4]);
					p.setMetadata("cost", new FixedMetadataValue(plugin, args[4]));
					p.sendMessage(ChatColor.GRAY + "Marking Player As ready to set warp.");
					p.setMetadata("RoomSetWarp", new FixedMetadataValue(plugin, true));
					p.sendMessage(ChatColor.GRAY + "Player Ready to set warp.");
					p.sendMessage(ChatColor.DARK_GREEN + "!!! " + ChatColor.GREEN + "Please click the door to add room" + ChatColor.DARK_GREEN + " !!!");
				}
			}
		}
	}
	public void teleportToRoom(Player p, String hotel, String roomnumber) {
		String prefix = "rm." +  hotel + "." + roomnumber;
		if (config.contains(prefix)) {
			Location warpLoc = new Location(p.getWorld(), config.getDouble(prefix + ".warpx"), config.getDouble(prefix + ".warpy"), config.getDouble(prefix + ".warpz"));
			Location doorLoc = new Location(p.getWorld(), config.getDouble(prefix + ".doorx"), config.getDouble(prefix + ".doory"), config.getDouble(prefix + ".doorz"));
			warpLoc = PlayerManager.lookAt(warpLoc, doorLoc);
			p.teleport(warpLoc);
		} else {
			p.openInventory(this.floors("gf", p));
		}
	}
	public void slamDoor(Location where) {
		Location torch = where.subtract(0, 2, 0);
		torch.getBlock().setType(Material.REDSTONE_TORCH_ON);
		torch.getBlock().setType(Material.AIR);
	}
	
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	public void roomExpiry(String hotel, String roomnumber){
		String prefix = "rm." +  hotel + "." + roomnumber;
		config.set(prefix + ".renter", null);
		config.set(prefix + ".datestamp", null);
	}
}
