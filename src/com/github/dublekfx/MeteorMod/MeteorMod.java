package com.github.dublekfx.MeteorMod;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorMod extends JavaPlugin {
	Player p;

	@Override
	public void onEnable()	{
		
	}
	
	@Override
	public void onDisable()	{
		
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)	{
		p = (Player) sender;
		Meteorite m;
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
}
