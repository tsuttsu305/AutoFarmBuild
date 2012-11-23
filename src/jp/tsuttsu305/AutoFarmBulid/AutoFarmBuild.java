package jp.tsuttsu305.AutoFarmBulid;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoFarmBuild extends JavaPlugin {
	public static AutoFarmBuild plugin;
	Logger logger = Logger.getLogger("Minecraft");


	public void onEnable(){
		getServer().getPluginManager().registerEvents(new PlayerClick(), this);
	}




}
