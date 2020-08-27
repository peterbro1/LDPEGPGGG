/*


,--.   ,--.                        ,--.   ,--.                                    ,--.             ,--.  ,--.     ,--.
 \  `.'  /,--,--.,--.--. ,---.      \  `.'  /,--,--.,--.--. ,---.                 |  | ,---.     ,-'  '-.|  ,---. `--' ,---.
  '.    /' ,-.  ||  .--'| .-. :      '.    /' ,-.  ||  .--'| .-. :                |  |(  .-'     '-.  .-'|  .-.  |,--.(  .-'
    |  | \ '-'  ||  |   \   --.        |  | \ '-'  ||  |   \   --..--..--..--.    |  |.-'  `)      |  |  |  | |  ||  |.-'  `)
    `--'  `--`--'`--'    `----'        `--'  `--`--'`--'    `----''--''--''--'    `--'`----'       `--'  `--' `--'`--'`----'      ,------.
  ,--.  ,--.                ,--.                  ,--.                             ,--.                                    ,--.  '  .--.  '
,-'  '-.|  ,---.  ,---.     |  |-.  ,---.  ,---.,-'  '-.    ,--. ,--.,---. ,--.,--.|  |,--.  ,--.,---.      ,---.  ,---. ,-'  '-.'--' _|  |
'-.  .-'|  .-.  || .-. :    | .-. '| .-. :(  .-''-.  .-'     \  '  /| .-. ||  ||  |`-'  \  `'  /| .-. :    | .-. || .-. |'-.  .-' .--' __'
  |  |  |  | |  |\   --.    | `-' |\   --..-'  `) |  |        \   ' ' '-' ''  ''  '      \    / \   --.    ' '-' '' '-' '  |  |   `---'
  `--'  `--' `--' `----'     `---'  `----'`----'  `--'      .-'  /   `---'  `----'        `--'   `----'    .`-  /  `---'   `--'   .---.
                                                            `---'                                          `---'                  '---'
 */
package me.gmx.objects;

import me.gmx.Bustabit;
import me.gmx.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ChatHandler implements Listener {
    private static ArrayList<Player> list;
    private Bustabit ins;
    public ChatHandler(Bustabit ins){
        this.ins = ins;
        list = new ArrayList<Player>();
        Bukkit.getPluginManager().registerEvents(this,ins);
    }

    public void addPlayer(Player player){
        if (list.contains(player) || ins.gui.bets.containsKey(player))
            return;
        player.sendMessage(Lang.MSG_TYPE_AMOUNT.toMsg());
        list.add(player);
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        new BukkitRunnable(){
            public void run(){
                if (!list.contains(player)){
                    return;
                }
                list.remove(player);
                if (player.isOnline())
                player.playSound(player.getLocation(),Sound.BLOCK_ANVIL_BREAK,1,1);
            }
        }.runTaskLater(Bustabit.getInstance(),200);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e){
        if (list.contains(e.getPlayer())){
            e.setCancelled(true);
            String s = ChatColor.stripColor(e.getMessage());

            s = s.replaceAll(",","").replaceAll("$","");
            try{
                long amt = Long.parseLong(s);
                if (ins.econ.getBalance(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId())) < amt){
                    list.remove(e.getPlayer());
                    e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.BLOCK_ANVIL_BREAK,1,1);
                    e.getPlayer().sendMessage(Lang.MSG_INSUFF_AMT.toMsg());
                    return;
                }
                e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.BLOCK_CLOTH_PLACE,1,1);
                e.getPlayer().sendMessage(Lang.MSG_BET_SUCCESS.toMsg().replace("%bet%",String.valueOf(amt)));
                ins.gui.addPlayer(e.getPlayer(),amt);
                ins.econ.withdrawPlayer(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()),amt);
                list.remove(e.getPlayer());
                ins.gui.open(e.getPlayer());

            }catch(Exception ex){
                list.remove(e.getPlayer());
                e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.BLOCK_ANVIL_BREAK,1,1);
                e.getPlayer().sendMessage(Lang.MSG_INVALID_AMT.toMsg());
                //ex.printStackTrace();
            }

        }
    }


    public void endCurrent(){
        for (Player p : list ){
            p.sendMessage(Lang.MSG_EXPIRED.toMsg());
        }
        list.clear();
    }
}
