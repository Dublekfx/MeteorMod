package com.github.dublekfx.MeteorMod;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Counter implements Runnable	{
	
	private Plugin plugin;
	private int duration;
	private Player pTarget;
	private int cooldown = 5;
		
	public Counter(Plugin pl, int time, Player target)	{
		plugin = pl;
		duration = time;
		pTarget = target;
	}
	public void countdown()	{
		Logger.getLogger("Minecraft").info("Countdown Started");
		int playerExp = pTarget.getTotalExperience();
		while (duration > 0)	{
			pTarget.setLevel(duration);
			this.counterTick();
		}
		while (cooldown > 0)	{
			this.counterTick();
		}
		pTarget.giveExp(playerExp);
	}
	private void counterTick()	{
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);	
	}
	@Override
	public void run() {
		duration--;
	}

}
