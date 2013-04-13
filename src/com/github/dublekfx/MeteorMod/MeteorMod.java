package com.github.dublekfx.MeteorMod;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorMod extends JavaPlugin implements Listener {
	private Player p;
	private Plugin plugin = this;
	
	private ArrayList<Meteorite> meteorites = new ArrayList<Meteorite>();

	@Override
	public void onEnable()	{
		getServer().getPluginManager().registerEvents(this, this);
		startReckoning(20*20);
	}
	
	@Override
	public void onDisable()	{
		stopReckoning();
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)	{
		p = (Player) sender;
		Player pTarget = null;
		Location target = null;
		int radius = -1;
		int countdown = -1;
		String material = "";
		boolean blockDamage = false;
		
		if (cmd.getName().equalsIgnoreCase("meteormod"))	{
			if ((sender instanceof Player && sender.hasPermission("meteor.launch")) || !(sender instanceof Player) )	{
				target = p.getTargetBlock(null, 128).getLocation();
				if (args.length == 0)	{
					sender.sendMessage("Cleaning up..");
					for (Meteorite m : meteorites)
						m.doHandlerUnregister();
					return false;
				}
				for (String s : args)	{
					if (s.substring(0, 2).equalsIgnoreCase("p:"))	{			//set target (player or crosshairs)
						pTarget = Bukkit.getPlayer(s.substring(2));
						target = pTarget.getLocation();
					}
					else if (s.substring(0, 2).equalsIgnoreCase("r:"))	{			//set radius
						radius = Integer.parseInt(s.substring(2));
					}
					else if (s.substring(0, 2).equalsIgnoreCase("e:"))	{			//set explosion block damage
						if (s.substring(2).equalsIgnoreCase("true") || s.substring(2).equalsIgnoreCase("false"))	
							blockDamage = Boolean.parseBoolean(s.substring(2));						
					}
					else if (s.substring(0, 2).equalsIgnoreCase("c:"))	{			//set countdown timer
						countdown = Integer.parseInt(s.substring(2));
					}
					else if (s.substring(0, 2).equalsIgnoreCase("m:"))	{
						material = s.substring(2);
					}
				}
				meteorites.add(new Meteorite(this, pTarget, target, material, radius, countdown, blockDamage));
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("meteor"))	{
			if (sender instanceof Player && sender.hasPermission("meteor.launch"))	{
				target = p.getTargetBlock(null, 128).getLocation();
				meteorites.add(new Meteorite(this, pTarget, target, material, radius, countdown, blockDamage));
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("startreckoning"))	{
			startReckoning(20*20);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("stopreckoning"))	{
			stopReckoning();
			return true;
		}
		return false;
	}
	
	public void startReckoning(long rLong)	{
		  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new scheduledReckoning(), rLong);	
		  }
	
	public void stopReckoning()	{
		Bukkit.getServer().getScheduler().cancelTasks(this);
	}



	public class scheduledReckoning	implements Runnable	{
		@Override
		public void run() {
			Player pTarget = getServer().getOnlinePlayers()[(int) (getServer().getOnlinePlayers().length * Math.random())];
			Location target = pTarget.getLocation();
			target.setX((int) ((160 * Math.random()) - 80));
			target.setZ((int) ((160 * Math.random()) - 80));
			int radius = -1;
			int countdown = -1;
			String material = "";
			boolean blockDamage = false;
		
			getLogger().info(pTarget.getName() + "has been randomly selected for termination");
			meteorites.add(new Meteorite(plugin, pTarget, target, material, radius, countdown, blockDamage));
			startReckoning((long) ((5*60*20 * Math.random()) + 5*60*20));
		}
	}
}