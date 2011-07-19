package net.milkbowl.combatevents.pvp;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.combatevents.CombatEventsCore;
import net.milkbowl.factionsex.FactionsEX;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatEventsPvP extends JavaPlugin {
	public static String plugName;
	public static Logger log = Logger.getLogger("Minecraft");

	//Dependencies
	private CombatEventsCore ceCore = null;
	public static Permission perms = null;
	public static Economy econ = null;
	public static FactionsEX factions;

	@Override
	public void onLoad() {
		plugName = "[" + this.getDescription().getName() + "]";
	}

	@Override
	public void onDisable() {
		log.info(plugName + " - " + "disabled!");
		Config.saveConfig(this);
	}

	protected Set<String> punishSet;
	
	@Override
	public void onEnable() {
		//If we can't load dependencies, disable
		if (!setupDependencies()) {
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupOptionals();
			
		
		punishSet = new HashSet<String>();
		//Initialize our configuration options
		Config.initialize(this);

		//Register our events & listeners
		PluginManager pm = this.getServer().getPluginManager();
		CombatListener combatListener = new CombatListener(ceCore, this);
		PvPPlayerListener playerListener = new PvPPlayerListener(this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, combatListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Monitor, this);
		log.info(plugName + " - v" + this.getDescription().getVersion() + " enabled!");
	}
	
	private void setupOptionals() {
		if (CombatEventsPvP.factions == null) {
			Plugin factions = this.getServer().getPluginManager().getPlugin("FactionsEx");
			if (factions != null) {
				CombatEventsPvP.factions = (FactionsEX) factions;
				log.info(plugName + " hooked into " + factions.getDescription().getName() + " v" + factions.getDescription().getVersion());
			}
		}
	}

	private boolean setupDependencies() {
		if (this.ceCore == null) {
			Plugin ceCore = this.getServer().getPluginManager().getPlugin("CombatEventsCore");
			if (ceCore != null) {
				this.ceCore = ((CombatEventsCore) ceCore);
			}
		} 
		if (CombatEventsPvP.econ == null || CombatEventsPvP.perms == null) {
			Plugin VAULT = this.getServer().getPluginManager().getPlugin("Vault");
			if (VAULT != null) {
				CombatEventsPvP.econ = Vault.getEconomy();
				CombatEventsPvP.perms = Vault.getPermission();
			}
		}
		if (CombatEventsPvP.perms == null || CombatEventsPvP.econ == null || ceCore == null)
			return false;
		else
			return true;
	}
}
