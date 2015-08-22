package us.arkyne.server.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import us.arkyne.nms.screenmessage.ScreenMessageAPI;
import us.arkyne.nms.zombie.ZombieClone;
import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.util.Util;

public class ArkynePlayer implements ConfigurationSerializable
{
	private UUID uuid;
	private OfflinePlayer player;
	
	private long lastPush = 0;
	private Joinable joinable;
	
	private Map<String, Object> extras = new HashMap<String, Object>();
	
	public ArkynePlayer(UUID uuid)
	{
		setUUID(uuid);
	}
	
	public void onLogin()
	{
		if (joinable == null)
		{
			ArkyneMain.getInstance().getMainLobbyHandler().getMainLobby().join(this);
		} else
		{
			setJoinableNoLeave(joinable);
		}
	}
	
	public void onLeave()
	{
		
	}
	
	public boolean isOnline()
	{
		return player.isOnline();
	}
	
	public boolean isManager()
	{
		if (isOnline())
		{
			return getOnlinePlayer().hasPermission("arkyne.manage");
		}
		
		return false;
	}
	
	public Player getOnlinePlayer()
	{
		return Bukkit.getPlayer(uuid);
	}
	
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
		this.player = Bukkit.getOfflinePlayer(uuid);
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	public String getChatName()
	{
		if (isOnline())
		{
			String points = ChatColor.RED + ".:" + ChatColor.BLUE + "1" + ChatColor.RED + ":. ";
			
			return points + getTitleName();
		}
		
		return "";
	}
	
	public String getTitleName()
	{
		if (isOnline())
		{
			String title = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(getOnlinePlayer()).getPrefix());
			
			return ChatColor.GRAY + title + getOnlinePlayer().getName();
		}
		
		return "";
	}
	
	public String getBracketedName()
	{
		if (isOnline())
		{
			String title = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(getOnlinePlayer()).getPrefix()));
			
			return ChatColor.GRAY + "[" + StringUtils.deleteWhitespace(title) + "] " + getOnlinePlayer().getName();
		}
		
		return "";
	}
	
	public void setJoinable(Joinable joinable)
	{
		if (this.joinable != null)
		{
			this.joinable.leave(this);
		}

		setJoinableNoLeave(joinable);
	}
	
	public void setJoinableNoLeave(Joinable joinable)
	{
		this.joinable = joinable;
		
		ArkyneMain.getInstance().getArkynePlayerHandler().hideShowPlayers(this, joinable.getPlayers());
		
		ScreenMessageAPI.sendTabHeader(getOnlinePlayer(), "\n" + ChatColor.GREEN + "You are playing on the Arkyne Network!", ChatColor.GREEN + "Check out our website!\n" + ChatColor.YELLOW + "ArkyneMC.com");
		
		getOnlinePlayer().setPlayerListName(getTitleName());
		getOnlinePlayer().setScoreboard(joinable.getScoreboard(this).getScoreboard());
		
		updateInventory();
		
		save();
	}
	
	public Joinable getJoinable()
	{
		return joinable;
	}
	
	public void updateInventory()
	{
		if (joinable != null && isOnline())
		{
			Inventory inv = joinable.getInventory(this);
			
			if (inv != null)
			{
				ItemStack[] itemStacks = new ItemStack[inv.getItems().length];
				ItemStack[] armorStacks = new ItemStack[4];
				
				for (int i = 0; i < itemStacks.length; i++)
				{
					itemStacks[i] = inv.getItem(i) != null ? inv.getItem(i).getItem() : null;
				}
				
				for (int i = 0; i < inv.getArmor().length; i++)
				{
					armorStacks[armorStacks.length - i - 1] = inv.getArmor(i) != null ? inv.getArmor(i).getItem() : null;
				}
				
				getOnlinePlayer().getInventory().setContents(itemStacks);
				getOnlinePlayer().getInventory().setArmorContents(armorStacks);
			}
		}
	}
	
	public Inventory getInventory()
	{
		return joinable != null ? joinable.getInventory(this) : null;
	}
	
	public void setExtra(String key, Object val)
	{
		extras.put(key, val);
	}
	
	public Object getExtra(String key)
	{
		return extras.get(key);
	}
	
	public boolean hasExtra(String key)
	{
		return extras.containsKey(key);
	}
	
	public void spawnClone()
	{
		Player player = getOnlinePlayer();
		Zombie zombie = (Zombie) getLocation().getWorld().spawnEntity(getLocation(), EntityType.ZOMBIE);
		
		ZombieClone.setZombieEquipment(zombie, new ItemStack[] {
				getInventory() != null ? getInventory().getItem(0).getItem() : null,
				
				player.getInventory().getBoots(),
				player.getInventory().getLeggings(),
				player.getInventory().getChestplate(),
				player.getInventory().getHelmet()
		});
		
		zombie.setCustomName(player.getName());
		zombie.setCustomNameVisible(true);
		
		zombie.setMetadata("ZombieClone", new FixedMetadataValue(ArkyneMain.getInstance(), true));
		zombie.setMetadata("CloneOwner", new FixedMetadataValue(ArkyneMain.getInstance(), player.getUniqueId().toString()));
	}
	
	public void freeze()
	{
		if (isOnline())
		{
			Player player = getOnlinePlayer();
			
			player.setFoodLevel(4);
			player.setWalkSpeed(0.0F);
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
		}
	}
	
	public void unfreeze()
	{
		if (isOnline())
		{
			Player player = getOnlinePlayer();
			
			player.setFoodLevel(20);
			player.setWalkSpeed(0.2F);
			player.removePotionEffect(PotionEffectType.JUMP);
		}
	}
	
	public Location getLocation()
	{
		if (isOnline())
		{
			return getOnlinePlayer().getLocation();
		}
		
		return null;
	}
	
	public void teleportRaw(Location loc)
	{
		if (isOnline())
		{
			Player player = getOnlinePlayer();
			
			if (player.isInsideVehicle())
			{
				Entity ent = player.getVehicle();
				
				player.leaveVehicle();
				ent.eject();
			}
			
			player.teleport(loc, TeleportCause.PLUGIN);
			
			player.setFallDistance(-1F);
			player.setVelocity(new Vector(0, 0, 0));
			
			player.setFireTicks(0);
			player.setHealth(getOnlinePlayer().getMaxHealth());
		}
	}
	
	public void teleport(Location loc)
	{
		teleport(loc, null);
	}
	
	public void teleport(Location loc, Callable<Void> call)
	{
		if (isOnline() && loc != null)
		{
			loc.getChunk().load(false);
			
			new BukkitRunnable()
			{
				public void run()
				{
					while (!loc.getChunk().isLoaded()) { }
					
					//Loaded chunk, most likely it is already loaded!
					
					new BukkitRunnable()
					{
						public void run()
						{
							teleportRaw(loc);
							
							try
							{
								if (call != null) call.call();
							} catch (Exception e) { }
						}
					}.runTask(ArkyneMain.getInstance());
				}
			}.runTaskAsynchronously(ArkyneMain.getInstance());
		}
	}
	
	public void pushTowards(Vector loc)
	{
		if (isOnline() && System.currentTimeMillis() - lastPush > 300)
		{
			Vector direction = loc.subtract(getLocation().toVector()).normalize();
			getOnlinePlayer().setVelocity(direction);
			
			if (getOnlinePlayer().isInsideVehicle())
			{
				getOnlinePlayer().getVehicle().setVelocity(direction.multiply(2D));
			}
			
			getOnlinePlayer().playSound(getLocation(), Sound.ARROW_HIT, 10, 1);
			
			lastPush = System.currentTimeMillis();
		}
	}
	
	public void sendMessage(String message)
	{
		sendMessage(message, ChatColor.AQUA);
	}
	
	public void sendMessage(String message, ChatColor msgColor)
	{
		sendMessageRaw(Util.PREFIX + msgColor + message);
	}
	
	public void sendMessageRaw(String message)
	{
		if (isOnline())
		{
			getOnlinePlayer().sendMessage(message);
		}
	}
	
	public void save()
	{
		ArkyneMain.getInstance().getArkynePlayerHandler().save(this);
	}
	
	public ArkynePlayer(Map<String, Object> map)
	{
		if (map.containsKey("joinable_type") && map.containsKey("joinable_id"))
		{
			Joinable.Type type = Joinable.Type.valueOf(map.get("joinable_type").toString());
			String id = map.get("joinable_id").toString();
			
			if (type == Joinable.Type.MAINLOBBY)
			{
				this.joinable = ArkyneMain.getInstance().getMainLobbyHandler().getMainLobby();
			} else if (type == Joinable.Type.MINIGAME || type == Joinable.Type.GAME)
			{
				Minigame minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(id);
				
				if (minigame != null)
				{
					String[] ids = id.split("-");
					
					this.joinable = type == Joinable.Type.GAME ? minigame.getGameHandler().getGame(Integer.parseInt(ids[1])) : minigame;
				} else
				{
					ArkyneMain.getInstance().getMinigameHandler().waitForMinigame(id, () ->
					{
						String[] ids = id.split("-");
						Minigame mg = ArkyneMain.getInstance().getMinigameHandler().getMinigame(id);
						
						this.joinable = type == Joinable.Type.GAME ? mg.getGameHandler().getGame(Integer.parseInt(ids[1])) : mg;
					});
				}
			}
		}
		
		/*
		if (map.containsKey("lobby"))
		{
			minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
			
			minigame.addPlayer(this);
		}
		*/
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (joinable != null)
		{
			map.put("joinable_id", joinable.getIdString());
			map.put("joinable_type", joinable.getType().name());
		}
		
		/*
		if (minigame != null)
		{
			map.put("minigame", minigame.getId());
		}
		*/
		
		return map;
	}
}