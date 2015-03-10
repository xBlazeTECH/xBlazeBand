package net.xblaze.xBlazeBand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Author: Lansing Nye-Madden
 * Changed: 2/16/14
 * Time: 4:00 PM EST
 */
public class HotelReminderTask extends BukkitRunnable {

	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private HotelManager hotelman;
	private BlazeBand plugin;

	public HotelReminderTask(BlazeBand plugin) {
		this.plugin = plugin;
		this.hotelman = plugin.hotelman;
	}
	
	@Override
	public void run() {
		notifyRooms("cr");
		notifyRooms("gf");
		notifyRooms("poly");
		notifyRooms("port");
	}
	public void notifyRooms(String hotel) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			try {
				for (int room = 0; room <= plugin.getConfig().getInt(hotel + "-max") ; room++) {
					try {
						String prefix = "rm." + hotel + ".";
						if (plugin.getConfig().contains(prefix + room + ".doorx") && plugin.getConfig().contains(prefix + room + ".doory") && plugin.getConfig().contains(prefix + room + ".doorz")) {
							if (plugin.getConfig().contains(prefix + room + ".renter")) {
								if (plugin.getConfig().getString(prefix + room + ".renter").equals(p.getName())) {
									Date now = Calendar.getInstance().getTime();
									Date then = null;
									try {
										then = sdf.parse((String) plugin.getConfig().get(prefix+room+".datestamp"));
									} catch (ParseException e) {
										e.printStackTrace();
									}
									if (getDateDiff(now, then, TimeUnit.SECONDS) <= 0) {
										p.sendMessage("Your room in the Grand Floridian just expired!");
										hotelman.roomExpiry(hotel, String.valueOf(room));
									}
								}
							}
						}
					} catch(NullPointerException ignore) {}
				}
			} catch (NullPointerException ignore) {}
		}
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
}
