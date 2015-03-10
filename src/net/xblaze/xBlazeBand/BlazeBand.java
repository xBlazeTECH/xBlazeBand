package net.xblaze.xBlazeBand;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.milkbowl.vault.economy.Economy;
import net.xblaze.xBlazeCore.BlazeCore;
import net.xblaze.xBlazeCore.api.nms.NmsManager;
import net.xblaze.xBlazeCore.api.types.ConsoleMessageType;
import net.xblaze.xBlazeCore.api.util.BungeeManager;
import net.xblaze.xBlazeCore.api.util.ConsoleManager;
import net.xblaze.xBlazeCore.api.util.InventoryManager;
import net.xblaze.xBlazeCore.api.util.ItemManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlazeBand extends JavaPlugin {

	/*
	 * Hook the xBlazeCore API.
	 */
	public ConsoleManager console = new ConsoleManager(this);
	public InventoryManager invman = new InventoryManager();
	public ItemManager itemman = new ItemManager();
	public NmsManager nmsman = new NmsManager();
	public BlazeCore blazecore;
	public BungeeManager bungeemanager;

	/*
	 * Create Local Instances.
	 */
	public HotelManager hotelman = new HotelManager(this);
	public MagicBandManager mbman = new MagicBandManager(this);
	public PlayerManager pman = new PlayerManager(this);
	public Menus menuman = new Menus(this);
	public DeveloperTools devtools = new DeveloperTools(this);

	/*
	 * All Hail Dependencies
	 */
	public static Economy econ = null;
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;
		BlazeBand.econ = rsp.getProvider();
		return BlazeBand.econ != null;
	}

	@Override
	public void onDisable() {
		console.log(ConsoleMessageType.INFO, " has been disabled sucessfully!");
	}

	@Override
	public void onEnable() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		blazecore = (BlazeCore) Bukkit.getPluginManager().getPlugin("xBlazeCore");
		if (!setupEconomy() ) {
			console.log(ConsoleMessageType.INFO, "I am missing a dependency: 'Vault'");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		this.saveDefaultConfig();
		this.saveConfig();
		Bukkit.getPluginManager().registerEvents(new CommandInterpreter(this), this);
		Bukkit.getPluginManager().registerEvents(new EventHandlers(this), this);
		// First Number is Delay, Second number is interval.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new HotelReminderTask(this), 120, (long) 1200);
		console.log(ConsoleMessageType.INFO, " has been enabled sucessfully!");
		bungeemanager = blazecore.bungeemanager;
	}

	@Override
	public void onLoad() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		console.log(ConsoleMessageType.INFO, " configuration loaded.");
	}

	public void sendPlayerToServer(Player p, String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
			p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "Looks like there was a problem sending you to the server you wanted to go to! :O");
		}
	}
}
