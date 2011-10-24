package net.milkbowl.combatevents.pvp;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PvPPlayerListener extends PlayerListener{

	private CombatEventsPvP plugin;
	public PvPPlayerListener(CombatEventsPvP plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (plugin.punishSet.contains(player.getName())) {
			plugin.punishSet.remove(player.getName());
			player.sendMessage("You have been killed while attempting to flee combat and your items have been looted!");
			Config.saveConfig(plugin);
		}	
	}
}
