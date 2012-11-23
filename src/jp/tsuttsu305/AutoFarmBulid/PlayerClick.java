package jp.tsuttsu305.AutoFarmBulid;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
			
			//ダイヤと金のくわで発動
			if (player.getItemInHand().getType() == Material.DIAMOND_HOE ||
					player.getItemInHand().getType() == Material.IRON_HOE){
				//click先のMaterialで判定
				switch(playerClickBl.getType()){
					case MOSSY_COBBLESTONE:	//苔石 - 小麦畑
						if (blockSearch(playerClickBl)){
							bulidWheat(playerClickBl);
							short du = player.getItemInHand().getDurability();
							player.getItemInHand().setDurability((short) (du+80));
						}
						break;

					default:
						break;
				}
			}
		}
		return;
	}

	//小麦畑生成
	public void bulidWheat(Block bl){
		//7x7
		//bl には苔石のBlock情報
		//地面の中心を取得
		Block center = bl.getRelative(0, -2, 0);
		int x=-3,  z=-3;
		//地面側を農地に置き換え
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				center.getRelative(x, 0, z).setType(Material.SOIL);
			}
		}

		//種植える
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				if (!(x == 0 && z == 0)){
					center.getRelative(x, 1, z).setType(Material.CROPS);
				}
			}
		}

		//中心を水源に置き換え。
		center.setType(Material.WATER);

		center.getRelative(BlockFace.UP).setType(Material.AIR);
		//苔石はドロップ
		center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).breakNaturally();
		return;
	}

	//地面側の中心Block情報を渡す
	public boolean blockSearch(Block bl){
		bl = bl.getRelative(0, -2, 0);
		int x=-3,  z=-3;
		//障害物がないか判定
		//土か草ブロック
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				if (!(bl.getRelative(x, 0, z).getType() == Material.DIRT || bl.getRelative(x, 0, z).getType() == Material.GRASS)){
					return false;
				}
			}
		}
		//上の空間が空気か判定
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				if (!(bl.getRelative(x, 1, z).getType() == Material.AIR)){
					if (x == 0 && z == 0){
						if (!(bl.getRelative(x, 1, z).getType() == Material.CLAY)){
							return false;
						}
					}else{
						return false;
					}
				}
			}
		}
		return true;
	}

}









