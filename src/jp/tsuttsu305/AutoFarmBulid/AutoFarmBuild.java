package jp.tsuttsu305.AutoFarmBulid;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AutoFarmBuild extends JavaPlugin {
	public static AutoFarmBuild plugin;
	Logger logger = Logger.getLogger("Minecraft");
	public boolean wgs = false;

	public void onEnable(){
		getServer().getPluginManager().registerEvents(new PlayerClick(this), this);

		if (getServer().getPluginManager().isPluginEnabled("WorldGuard")){
			//WorldGuard使用フラグ
			wgs = true;
			logger.info("AutoFarmBulid hooked to WorldGuard");
		}else{
			wgs = false;
		}
	}
	//WorldGuard使用時
		private WorldGuardPlugin getWorldGuard() {
			Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
			// WorldGuard may not be loaded
			if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
				return null; // Maybe you want throw an exception instead
			}
			return (WorldGuardPlugin) plugin;
		}
		public WorldGuardPlugin wg(){
			return getWorldGuard();

		}
}
