package com.github.dublekfx.MeteorMod;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Meteorite implements Listener	{
	
	private Plugin plugin;
	private int countdown;
	private int radius;
	private final int DEFAULT_RADIUS = 3;
	private final int DEFAULT_COUNTDOWN = 20;
	protected Location target;
	private Location skyTarget;
	private Player pTarget;
	private boolean executeCountdown;
	private boolean falling;
	private boolean explosionBlockDamage;
	private Material mat;
	private final Material DEFAULT_MAT = Material.NETHERRACK;
	private ArrayList<Location> sphereCoords = new ArrayList<Location>();
	protected ArrayList<UUID> blockID = new ArrayList<UUID>();
	private Counter count;
	
	public Meteorite(Plugin pl, Player pT, int c)	{
		pTarget = pT;
		countdown = c;
		plugin = pl;
	}
	public Meteorite(Plugin pl, Player pT, Location xyz, String m, int r, int c, boolean explode)	{
		if (pT != null)	{
			pTarget = pT;
		}
		target = xyz;
		
		this.defaultMeteorite();
		
		if (r != -1)	{
			radius = r;
		}
		if (!(m.equalsIgnoreCase("")))	{
			Material.matchMaterial(m);
		}
		if (c != -1)	{
			countdown = c;
			executeCountdown = true;
		}
		explosionBlockDamage = explode;
		plugin = pl;
	}
	
	public void defaultMeteorite()	{
		//target handled by constructor
		radius = DEFAULT_RADIUS;
		skyTarget = target;		//did I just alias?
		target.setY(target.getWorld().getHighestBlockAt(target).getY());
		skyTarget.setY(255 - radius);
		falling = false;
		executeCountdown = false;
		mat = DEFAULT_MAT;
		countdown = DEFAULT_COUNTDOWN;
	}
	public boolean isFalling()	{
		return falling;
	}
	public void setFalling(boolean drop)	{
		falling = drop;
	}
		
	public void dropMeteorite()	{
		if (executeCountdown == true)	{
			count = new Counter(plugin, countdown, pTarget);
			count.countdown();
			executeCountdown = false;
		}
		if (sphereCoords.size() >= 1 && this.isFalling() && executeCountdown == false)	{	//Ensures that genSphereCoords has been run, and falling = true
			//target.getBlock().setType(Material.LAPIS_BLOCK);
			for (Location a : sphereCoords)	{			
				a.getBlock().setType(Material.AIR);
				blockID.add(skyTarget.getWorld().spawnFallingBlock(a, mat, (byte) 0).getUniqueId());
			}			
			Logger.getLogger("Minecraft").info("Let Chaos Reign!");
		}
		else
			Logger.getLogger("Minecraft").info("Preconditions not met!");
	}
	
	public void genMeteorite()	{	//Creates a floating ball. dropMeteor must be called separately
		sphereCoords = this.genSphereCoords(radius);
		sphereCoords.removeAll(this.genSphereCoords(radius - 1));
		for (Location a : sphereCoords)	{			
				a.getBlock().setType(mat);
		}
	}
	
	private ArrayList<Location> genSphereCoords(int r)	{
		ArrayList<Location> coords = new ArrayList<Location>();
		double bpow = Math.pow(r, 2);
		double bx = skyTarget.getX();
		double by = skyTarget.getY();
		double bz = skyTarget.getZ();
		
		 for (int z = 0; z <= r; z++) {
	            double zpow = Math.pow(z, 2);
	            for (int x = 0; x <= r; x++) {
	                double xpow = Math.pow(x, 2);
	                for (int y = 0; y <= r; y++) {
	                    if ((xpow + Math.pow(y, 2) + zpow) <= bpow) {
	                    	coords.add(new Location(skyTarget.getWorld(), bx + x, by + y, bz + z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx + x, by + y, bz - z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx - x, by + y, bz + z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx - x, by + y, bz - z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx + x, by - y, bz + z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx + x, by - y, bz - z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx - x, by - y, bz + z));
	                    	coords.add(new Location(skyTarget.getWorld(), bx - x, by - y, bz - z));
	                    }
	                }
	            }
		 }
		 return coords;
	}
	public void explode(Location loc)	{
		target.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4F, false, explosionBlockDamage);
		//Logger.getLogger("Minecraft").info("KaBOOM");
	}
	

	public class scheduledCounter implements Runnable	{

		@Override
		public void run() {
			
		}
		
	}
}
