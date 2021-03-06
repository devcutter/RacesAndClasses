/*
 * Races - by tobiyas
 * http://
 *
 * powered by Kickstarter
 */

package de.tobiyas.racesandclasses.listeners.generallisteners;


import static de.tobiyas.racesandclasses.translation.languages.Keys.login_no_race_selected;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.APIs.LanguageAPI;
import de.tobiyas.racesandclasses.configuration.member.file.MemberConfig;
import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.gui.HolderInventory;
import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.race.RaceContainer;
import de.tobiyas.racesandclasses.util.consts.Consts;


public class Listener_Player implements Listener {
	private RacesAndClasses plugin;

	public Listener_Player(){
		plugin = RacesAndClasses.getPlugin();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLeave(PlayerQuitEvent event){
		if(plugin.getConfigManager().getGeneralConfig().isConfig_channels_enable()){
			Player player = event.getPlayer();
			plugin.getChannelManager().playerQuit(player);
		}
	}
	 

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		boolean racesActive = plugin.getConfigManager().getGeneralConfig().isConfig_enableRaces();
		
		RaceContainer container = (RaceContainer) plugin.getRaceManager().getHolderOfPlayer(player.getName());
		if((container == null || container == plugin.getRaceManager().getDefaultHolder()) 
				&& racesActive){
			LanguageAPI.sendTranslatedMessage(player, login_no_race_selected);
			if(container == null){
				plugin.getRaceManager().addPlayerToHolder(player.getName(), Consts.defaultRace, true);
			}
		}
		
		plugin.getPlayerManager().checkPlayer(player.getName());
		plugin.getConfigManager().getMemberConfigManager().getConfigOfPlayer(player.getName());
		if(plugin.getConfigManager().getGeneralConfig().isConfig_channels_enable()){
			plugin.getChannelManager().playerLogin(player);
		}
		
		if(racesActive){
			container.editTABListEntry(player.getName());
		}
		
		boolean forceSelectOfRace = plugin.getConfigManager().getGeneralConfig().isConfig_openRaceSelectionOnJoinWhenNoRace();
		boolean playerHasNoRace = plugin.getRaceManager().getHolderOfPlayer(player.getName()) == plugin.getRaceManager().getDefaultHolder();
		int scheduledTimeToOpen = plugin.getConfigManager().getGeneralConfig().getConfig_debugTimeAfterLoginOpening();
		
		if(playerHasNoRace && forceSelectOfRace && racesActive){
			final HolderInventory raceInv = new HolderInventory(player, plugin.getRaceManager());
			if(raceInv.getNumberOfHolder() > 0){
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						player.openInventory(raceInv);
					}
				}, scheduledTimeToOpen * 20);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event){
		if(plugin.getConfigManager().getGeneralConfig().isConfig_channels_enable()){
			World oldWorld = event.getFrom();
			Player player = event.getPlayer();
			plugin.getChannelManager().playerChangedWorld(oldWorld, player);
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		if(!plugin.getConfigManager().getGeneralConfig().isConfig_channels_enable()) return;
		String orgMsg = event.getMessage();
		Player player = event.getPlayer();
		
		if(orgMsg.charAt(0) == '/') return;
		event.setCancelled(true);
		
		MemberConfig config = plugin.getConfigManager().getMemberConfigManager().getConfigOfPlayer(player.getName());
		String channel = "Global";
		
		if(config != null){
			channel = config.getCurrentChannel();
			if(!plugin.getChannelManager().isMember(player.getName(), channel)){
				player.sendMessage(ChatColor.RED + "You are writing in a channel, you don't have access to. Please change your channel with " + 
									ChatColor.LIGHT_PURPLE + "/channel change" + ChatColor.YELLOW + " [channelname]");
				return;
			}
		}

		plugin.getChannelManager().broadcastMessageToChannel(channel, player, orgMsg);
	}
	
}
