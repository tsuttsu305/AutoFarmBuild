package net.tsuttsu305.AutoFarmBulid;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class PlayerClick implements Listener  {
	private AutoFarmBuild afb = null;

	public PlayerClick(AutoFarmBuild afb) {
		this.afb = afb;
	}



	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event){
		//右クリックか左クリックかを判定
		//右でブロックをクリックした場合のみ実行する
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			//クリックしたブロックの取得
			Block playerClickBl = event.getClickedBlock();
			//Playerの取得
			Player player = event.getPlayer();

			//ダイヤくわで発動
			if (player.getItemInHand().getType() == Material.DIAMOND_HOE){
				//click先のMaterialで判定
				switch(playerClickBl.getType()){
					case MOSSY_COBBLESTONE:	//苔石 - 小麦畑
						if (blockSearch(playerClickBl, player)){
							if (player.getItemInHand().getDurability()+80 <= 1563){
								bulidFarm(playerClickBl, Material.CROPS, player);
								short du = player.getItemInHand().getDurability();
								player.getItemInHand().setDurability((short) (du+80));
							}else{
								player.sendMessage(ChatColor.RED + "くわの耐久値が足りません!!!");
							}
						}
						break;
					case BRICK://レンガ　－　ほてと
						if (blockSearch(playerClickBl, player)){
							if (player.getItemInHand().getDurability()+80 <= 1563){
								bulidFarm(playerClickBl, Material.POTATO, player);
								short du = player.getItemInHand().getDurability();
								player.getItemInHand().setDurability((short) (du+80));
							}else{
								player.sendMessage(ChatColor.RED + "くわの耐久値が足りません!!!");
							}
						}
						break;
					case NETHER_BRICK: //ネザーのレンガ　－　人参
						if (blockSearch(playerClickBl, player)){
							if (player.getItemInHand().getDurability()+80 <= 1563){
								bulidFarm(playerClickBl, Material.CARROT, player);
								short du = player.getItemInHand().getDurability();
								player.getItemInHand().setDurability((short) (du+80));
							}else{
								player.sendMessage(ChatColor.RED + "くわの耐久値が足りません!!!");
							}
						}
						break;
					case BOOKSHELF: //本棚 - サトウキビ
						if (blockSearchEx(playerClickBl, player)){
							if (player.getItemInHand().getDurability()+100 <= 1563){
								sato(playerClickBl, player);
								short du = player.getItemInHand().getDurability();
								player.getItemInHand().setDurability((short) (du+100));
							}else{
								player.sendMessage(ChatColor.RED + "くわの耐久値が足りません!!!");
							}
						}

					default:
						break;
				}
			}
		}
		return;
	}

	//畑生成 小麦　じゃがいも　人参
	public void bulidFarm(Block bl, Material ma, Player pl){
		//7x7
		//bl には苔石のBlock情報
		//地面の中心を取得
		Block center = bl.getRelative(0, -2, 0);
		int x=-3,  z=-3;
		//地面側を農地に置き換え
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				BlockState bf = center.getRelative(x, 0, z).getState();
				center.getRelative(x, 0, z).setType(Material.SOIL);
				//HawkEye判定
				if (afb.hawkeyeFlag == true){
					//ロギング
					hawkPl(pl,bf,  center.getRelative(x, 0, z).getState());
				}
			}
		}

		//種植える
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				if (!(x == 0 && z == 0)){
					//BlockState bf = center.getRelative(x, 1, z).getState();
					center.getRelative(x, 1, z).setType(ma);
					//HawkEye判定
					/*if (afb.hawkeyeFlag == true){
						//ロギング
						hawkPl(pl,bf,  center.getRelative(x, 1, z).getState());
					}*/
				}
			}
		}

		//中心を水源に置き換え。
		BlockState cen = center.getState();
		center.setType(Material.WATER);
		//HawkEye判定
		if (afb.hawkeyeFlag == true){
			//ロギング
			hawkPl(pl,cen,  center.getState());
		}
		
		//土設置
		BlockState cenUP = center.getRelative(BlockFace.UP).getState();
		center.getRelative(BlockFace.UP).setType(Material.DIRT);
		if (afb.hawkeyeFlag == true){
			//ロギング
			hawkPl(pl,cenUP,  center.getRelative(BlockFace.UP).getState());
		}
		
		//苔石はドロップ
		center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).breakNaturally();
		
		//松明建てる
		BlockState cenUPUP = center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getState();
		center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.TORCH);
		if (afb.hawkeyeFlag == true){
			//ロギング
			hawkPl(pl,cenUPUP,  center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getState());
		}
		
		return;
	}

	//地面側の中心Block情報を渡す
	public boolean blockSearch(Block bl, Player pl){
		bl = bl.getRelative(0, -2, 0);
		int x=-3,  z=-3;
		//障害物がないか判定
		//土か草ブロック
		for (x = -3;x<=3;x++){
			for (z = -3;z<=3;z++){
				//wgフラグチェック
				if (afb.wgs == true){
					if(afb.wg().canBuild(pl, bl.getRelative(x, 0, z))){
						switch(bl.getRelative(x, 0, z).getType()){
							case DIRT:
							case GRASS:
								break;
							default:
								return false;
						}
					}else{
						pl.sendMessage(ChatColor.RED + "建設権限がない領域があります!!!");
						return false;
					}
				}else{
					switch(bl.getRelative(x, 0, z).getType()){
						case DIRT:
						case GRASS:
							break;
						default:
							return false;
					}
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

	//地面側の中心Block情報を渡す
	public boolean blockSearchEx(Block bl, Player pl){
		bl = bl.getRelative(0, -2, 0);
		int x=-4,  z=-4;
		//障害物がないか判定
		//土か草ブロック
		for (x = -4;x<=4;x++){
			for (z = -4;z<=4;z++){
				//wgフラグチェック
				if (afb.wgs == true){
					if(afb.wg().canBuild(pl, bl.getRelative(x, 0, z))){
						switch(bl.getRelative(x, 0, z).getType()){
							case DIRT:
							case GRASS:
							case SAND:
								break;
							default:
								return false;
						}
					}else{
						pl.sendMessage(ChatColor.RED + "建設権限がない領域があります!!!");
						return false;
					}
				}else{
					switch(bl.getRelative(x, 0, z).getType()){
						case DIRT:
						case GRASS:
						case SAND:
							break;
						default:
							return false;
					}
				}
			}
		}
		//上の空間が空気か判定
		for (x = -4;x<=4;x++){
			for (z = -4;z<=4;z++){
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

	//サトウキビ
	public void sato(Block bl, Player pl){
		//4x4
		//地面の中心を取得
		Block center = bl.getRelative(0, -2, 0);
		//水設置
		for (int x = -4; x <= 4;x++){
			for (int z = -4;z <= 4;z++){
				if (Math.abs(z) % 3 == 0){
					BlockState bf = center.getRelative(x, 0, z).getState();
					center.getRelative(x, 0, z).setType(Material.WATER);
					//HawkEye判定
					if (afb.hawkeyeFlag == true){
						//ロギング
						hawkPl(pl,bf, center.getRelative(x, 0, z).getState());
					}
				}
			}
		}
		//サトウキビ植える
		for (int x = -4; x <= 4;x++){
			for (int z = -4;z <= 4;z++){
				if (Math.abs(z) % 3 == 0){
					BlockState bf = center.getRelative(x, 1, z).getState();
					center.getRelative(x, 1, z).setType(Material.WOOD_STEP);
					//HawkEye判定
					if (afb.hawkeyeFlag == true){
						//ロギング
						hawkPl(pl,bf,  center.getRelative(x, 1, z).getState());
					}
				}else{
					//BlockState bf = center.getRelative(x, 1, z).getState();
					center.getRelative(x, 1, z).setType(Material.SUGAR_CANE_BLOCK);
					//HawkEye判定
					/*if (afb.hawkeyeFlag == true){
						//ロギング
						hawkPl(pl,bf,  center.getRelative(x, 1, z).getState());
					}*/
				}
			}
		}
		center.getRelative(BlockFace.UP).setType(Material.WOOD_STEP);
		//本棚破壊
		center.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.AIR);

	}
	//hawk 設置
	public void hawkPl (Player pl, BlockState bf, BlockState af){
		if(afb.hawkeyeFlag) {
			HawkEyeAPI.addEntry(afb, new BlockChangeEntry(pl, DataType.BLOCK_PLACE, bf.getLocation(), bf, af));
		}
	}
}