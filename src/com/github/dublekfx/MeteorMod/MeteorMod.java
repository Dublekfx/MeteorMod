package com.github.dublekfx.MeteorMod;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorMod extends JavaPlugin implements Runnable, Listener {
	Player p;
	Meteorite m;

	@Override
	public void onEnable()	{
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable()	{
		
	}
	
/*	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)	{
		p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("meteor"))	{
			if ((sender instanceof Player && sender.hasPermission("meteor.launch")) )	{
				if (args.length == 0)	{
					m = new Meteorite(p.getTargetBlock(null, 128).getLocation());
					m.genMeteorite();
					m.setFalling(true);
					m.dropMeteorite();
					return true;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("meteormod"))	{
			if ((sender instanceof Player && sender.hasPermission("meteor.launch")) )	{
				if (args.length == 1)	{
					m = new Meteorite(Bukkit.getPlayer(args[0]).getLocation());
					m.genMeteorite();
					m.setFalling(true);
					m.dropMeteorite();
					return true;
				}
				else if (args.length == 2)	{
					m = new Meteorite(Bukkit.getPlayer(args[0]).getLocation(), Integer.parseInt(args[1]));
					m.genMeteorite();
					m.setFalling(true);
					m.dropMeteorite();
					return true;
				}
			}
			if (!(sender instanceof Player))	{
				if (args.length == 1)	{
					m = new Meteorite(Bukkit.getPlayer(args[0]).getLocation());
					m.genMeteorite();
					m.setFalling(true);
					m.dropMeteorite();
					return true;
				}
				else if (args.length == 2)	{
					m = new Meteorite(Bukkit.getPlayer(args[0]).getLocation(), Integer.parseInt(args[1]));
					m.genMeteorite();
					m.setFalling(true);
					m.dropMeteorite();
					return true;
				}
			}
		}
		return false;
	}
*/	

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
				m = new Meteorite(pTarget, target, material, radius, countdown, blockDamage);
				m.genMeteorite();
				m.setFalling(true);
				m.dropMeteorite();
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("meteor"))	{
			if (sender instanceof Player && sender.hasPermission("meteor.launch"))	{
				target = p.getTargetBlock(null, 128).getLocation();
				System.out.println("meteor");
				m = new Meteorite(pTarget, target, material, radius, countdown, blockDamage);
				m.genMeteorite();
				m.setFalling(true);
				m.dropMeteorite();
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("countdown"))	{
			if (sender instanceof Player && args.length == 1)	{
				m = new Meteorite(p, Integer.parseInt(args[0]));
				m.countdown();
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event){
		//Logger.getLogger("Minecraft").info("Meteor in position! (EntityChangeBlockEvent)");
		try {
			for (UUID u : m.blockID)	{
				if (u.equals(event.getEntity().getUniqueId()))	{
					//Logger.getLogger("Minecraft").info("Meteor in position!");
					m.explode(event.getBlock().getLocation());
					event.getBlock().setType(Material.AIR);
					return;
				}
			}
		} catch (NullPointerException e) {
		}

	}
	
//		public void startReckoning()	{
//			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Meteorite()/*<- your task*/, 200L/*<- start delay in ticks*/, 100L/*repeat delay in ticks*/);
//		}
	
//	public void stopReckoning()	{
//		Bukkit.getServer().getScheduler().cancelTasks(this);
//	}

	@Override
	public void run() {
		
	}
}
