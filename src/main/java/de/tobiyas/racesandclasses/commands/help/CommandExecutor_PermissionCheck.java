package de.tobiyas.racesandclasses.commands.help;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.AbstractTraitHolder;
import de.tobiyas.racesandclasses.util.consts.PermissionNode;

public class CommandExecutor_PermissionCheck implements CommandExecutor {

	/**
	 * The plugin to call stuff on
	 */
	private RacesAndClasses plugin;
	
	/**
	 * Creates a CommandExecutor to call when command should be executed.
	 */
	public CommandExecutor_PermissionCheck() {
		plugin = RacesAndClasses.getPlugin();
		try{
			plugin.getCommand("racpermcheck").setExecutor(this);
		}catch(Exception e){
			plugin.log("ERROR: Could not register command /racpermcheck.");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length == 1 && "race".equalsIgnoreCase(args[0])){
			listRacePermissions(sender);
			return true;
		}

		if(args.length == 1 && "class".equalsIgnoreCase(args[0])){
			listClassPermissions(sender);
			return true;
		}
		if(args.length == 1 && "class".equalsIgnoreCase(args[0])){
			listCommandPermissions(sender);
			return true;
		}
		
		
		return true;
	}
	
	
	/**
	 * Checks Permissions for specific Race.
	 * 
	 * @param sender
	 */
	private void listRacePermissions(CommandSender sender){
		if(!plugin.getConfigManager().getGeneralConfig().isConfig_enableRaces()){
			sender.sendMessage("[RaC] Races are Disabled.");
			return;
		}
		
		sender.sendMessage(ChatColor.RED + "=====" + ChatColor.YELLOW + " Race Permissions: " + ChatColor.RED + "=====");
		
		List<String> holders = plugin.getRaceManager().listAllVisibleHolders();
		for(String holderName : holders){
			AbstractTraitHolder holder = plugin.getRaceManager().getHolderByName(holderName);
			
			boolean senderContained = holder.containsPlayer(sender.getName());
			boolean hasPermissions = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.racePermPre + holderName);
			
			sender.sendMessage(ChatColor.BLUE + holder.getName() + ": " + (senderContained ? ChatColor.LIGHT_PURPLE + " (Your Race)" : ""
					+ (hasPermissions? ChatColor.GREEN + " Permissions" : ChatColor.RED + " No Permissions")));
		}
		
		if(holders.isEmpty()){
			sender.sendMessage(ChatColor.RED + "You have access to no Race.");
		}
	}
	
	
	/**
	 * Checks Permissions for Specific Classes.
	 * 
	 * @param sender
	 */
	private void listClassPermissions(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "=====" + ChatColor.YELLOW + " Class Permissions: " + ChatColor.RED + "=====");
		
		List<String> holders = plugin.getClassManager().listAllVisibleHolders();
		for(String holderName : holders){
			AbstractTraitHolder holder = plugin.getClassManager().getHolderByName(holderName);
			
			boolean senderContained = holder.containsPlayer(sender.getName());
			boolean hasPermissions = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.classPermPre + holderName);
			
			sender.sendMessage(ChatColor.BLUE + holder.getName() + ": " + (senderContained ? ChatColor.LIGHT_PURPLE + " (Your Class)" : ""
					+ (hasPermissions? ChatColor.GREEN + " Permissions" : ChatColor.RED + " No Permissions")));
		}
		
		if(holders.isEmpty()){
			sender.sendMessage(ChatColor.RED + "You have access to no Class.");
		}
	}
	
	
	/**
	 * Lists permissions to all commands.
	 * 
	 * @param sender
	 */
	private void listCommandPermissions(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "=====" + ChatColor.YELLOW + " Command Permissions: " + ChatColor.RED + "=====");
		
		boolean raceSelectPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.selectRace);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/race select' " 
				+ (raceSelectPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));

		boolean raceChangePermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.changeRace);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/race change' " 
				+ (raceChangePermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
		
		boolean classSelectPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.selectClass);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/class select' " 
				+ (classSelectPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
		
		boolean classChangePermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.changeClass);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/class change' " 
				+ (classChangePermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
	
		boolean healSelfPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.healSelf);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/racesheal' " 
				+ (healSelfPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
	
		boolean healOthersPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.healOther);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/raceheal <playername>' " 
				+ (healOthersPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
	
		boolean setGodmodePermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.god);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/racesgod [playername]' " 
				+ (setGodmodePermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
		
		boolean reloadPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.reload);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/racesreload [-gc]' " 
				+ (reloadPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
		
		boolean debugPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.debug);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/racesdebug [subcommand]' " 
				+ (debugPermission ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));
		
		boolean statisticsPermission = plugin.getPermissionManager().checkPermissionsSilent(sender, PermissionNode.debug);
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "'/racesstatistics [plugin / subcommand]' " 
				+ (statisticsPermission? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));	
		
	}

}
