package jp.tsuttsu305.AutoFarmBulid;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClick implements Listener {

	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event){
		//右クリックか左クリックかを判定
		//右でブロックをクリックした場合のみ実行する
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			//クリックしたブロックの取得
			Block playerClickBl = event.getClickedBlock();
			//Playerの取得
			Player player = event.getPlayer();
			

			switch(playerClickBl.getType()){
				case MOSSY_COBBLESTONE:
					break;

				default:
					break;


			}

		}
	}

	//小麦畑生成
	public boolean bulidWheat(Block bl){

		return false;
	}

}
