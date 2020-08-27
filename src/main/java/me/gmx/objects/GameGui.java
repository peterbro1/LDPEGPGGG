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
import me.gmx.config.Settings;
import me.gmx.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameGui {
    private Bustabit ins;
    private double multi;
    private LinkedList<Double> history;
    public HashMap<Player,Long> bets;
    private HashMap<Player,Double> outtings;
    private GuiMenu menu;
    public GameState state;
    private int timeUntilNext = 0;
    private int buttonSlot = 4;
    public GameGui(Bustabit ins){
        this.ins = ins;
        multi = 0;
        this.state = GameState.DEAD;
        outtings = new HashMap<>();
        history = new LinkedList<Double>();
        bets = new HashMap<Player,Long>();
        menu = new GuiMenu(6, Lang.GUI_TITLE.toString());
        menu.setBorder(1, new ItemStack(Material.STAINED_GLASS_PANE,1,(short)2));

        startTimer();

    }


    public void startTimer(){
        this.state = GameState.IDLE;
        outtings.clear();
        bets.clear();
        timeUntilNext=Settings.TIME_BETWEEN_GAMES.getNumber();
        GuiButton GAME = new GuiButton("timer_button",Material.NETHER_STAR,Lang.BUTTON_NAME.toString(),buttonSlot,Lang.BUTTON_LORE.getStringList()){
            @Override
            public void executeAction(GuiMenu m,Player p){
                if (state != GameState.IDLE)
                    return;

                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                ins.chand.addPlayer(p);
            }
        };
        ItemStack stack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY,1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Lang.PREVIOUS_CRASHES_NAME.toString());

        try {
            meta.setLore(Arrays.asList(Lang.HISTORY_MULTIPLIER_TEXT.toString().replace("%multiplier%",history.getFirst().toString())));
            stack.setItemMeta(meta);
            menu.getInventory().setItem(47, stack);

            meta.setLore(Arrays.asList(Lang.HISTORY_MULTIPLIER_TEXT.toString().replace("%multiplier%",history.get(1).toString())));
            stack.setItemMeta(meta);
            menu.getInventory().setItem(48, stack);
            meta.setLore(Arrays.asList(Lang.HISTORY_MULTIPLIER_TEXT.toString().replace("%multiplier%",history.get(2).toString())));
            stack.setItemMeta(meta);
            menu.getInventory().setItem(49, stack);
            meta.setLore(Arrays.asList(Lang.HISTORY_MULTIPLIER_TEXT.toString().replace("%multiplier%",history.get(3).toString())));
            stack.setItemMeta(meta);
            menu.getInventory().setItem(50, stack);
            meta.setLore(Arrays.asList(Lang.HISTORY_MULTIPLIER_TEXT.toString().replace("%multiplier%",history.get(4).toString())));
            stack.setItemMeta(meta);
            menu.getInventory().setItem(51, stack);
        }catch(Exception e){
            for (int i = 47;i<52;i++){
                if (menu.getInventory().getItem(i) == null)
                    menu.getInventory().setItem(i,stack);
            }
        }

        GuiButton book = new GuiButton("book",Material.BOOK,Lang.BOOK_TITLE.toString(),53,Lang.BOOK_LORE.getStringList());
        menu.addButton(book);
        menu.addButton(GAME);

        new BukkitRunnable(){
            public void run(){
                timeUntilNext--;
                menu.changeButtonLore(GAME,"Next game in: " + timeUntilNext);
                if (timeUntilNext == 0){
                    this.cancel();
                    startGame();
                }

            }
        }.runTaskTimer(ins,0,20);
    }

    public void addPlayer(Player p, long bet){
        if (bets.containsKey(p) || bet <1) {

            return;
        }

        bets.put(p,bet);
        menu.addPlayers(bets);
    }

    public void removePlayer(Player p){
        if (!bets.containsKey(p))
            return;

        bets.remove(p);
        menu.addPlayers(bets);
    }

    public void open(Player player){
        menu.open(player);
    }

    public String getHistory(){
        String s = ""+ ChatColor.RESET;
        for (Double d : history)
            s+=String.valueOf(ChatUtils.round(d,2)) + " ";
        return s;
    }




    public void startGame(){
        ins.chand.endCurrent();
        this.state = GameState.RUNNING;
        timeUntilNext=0;

        final double crash = getResult();
        GuiButton GAMEa = new GuiButton("running_button",Material.NETHER_STAR
                , Lang.BUTTON_NAME_RUNNING.toString(),buttonSlot, ChatColor.RED + "1"){
            @Override
            public void executeAction(GuiMenu m,Player p){

                if(bets.containsKey(p) && !outtings.containsKey(p)){
                    if (state == GameState.RUNNING){
                        outtings.put(p,multi);
                        ins.econ.depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()),outtings.get(p) * bets.get(p));
                        p.sendMessage(Lang.MSG_WINNER.toMsg()
                                .replace("%amount%",String.valueOf(ChatUtils.round(multi * bets.get(p),2))));
                        p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1,1);
                        removePlayer(p);

                        if (multi >= 10){
                            Bukkit.broadcastMessage(Lang.BROADCAST_MSG.toMsg()
                            .replace("%multiplier%",String.valueOf(multi)+"x")
                            .replace("%player%",p.getName())
                            .replace("%win%",String.valueOf(ChatUtils.round(multi * bets.get(p),2))));
                        }

                    }else if (state == GameState.DEAD){
                        p.playSound(p.getLocation(),Sound.BLOCK_ANVIL_BREAK,1,1);

                    }

                }
            }
        };
        menu.addButton(GAMEa);
        multi = 1;
        new BukkitRunnable(){
            public void run(){
                multi+=multi/50;
                if (multi >= crash) {
                    multi = crash;
                    state = GameState.DEAD;
                    menu.clearPlayers();
                    this.cancel();
                    menu.changeButtonLore(GAMEa,"&4&l"+ChatUtils.round(multi,2)+"x");
                    for (HumanEntity e : menu.getInventory().getViewers()){
                        if (e instanceof Player)
                            ((Player)e).playSound(e.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,5,1);
                    }
                    //executeOrders();
                    bets.keySet().forEach(a -> a.sendMessage(Lang.MSG_LOSE.toMsg().replace("%bet%",String.valueOf(bets.get(a)))));

                    Bukkit.getScheduler().scheduleSyncDelayedTask(ins, new BukkitRunnable() {
                        public void run() {
                            startTimer();
                            history.addFirst(ChatUtils.round(crash,2));
                            if (history.size() > Settings.HISTORY_LENGTH.getNumber())
                                history.removeLast();

                        }
                    }, 40);
                }else
                menu.changeButtonLore(GAMEa,ChatColor.RED + ""+ChatUtils.round(multi,2)+"x");
                //menu.addButton(GAME);



            }
        }.runTaskTimer(ins,0,2);

    }

    private void executeOrders(){
        for (Player p : outtings.keySet()){
            ins.econ.depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()),outtings.get(p) * bets.get(p));
            p.sendMessage(Lang.MSG_WINNER.toMsg()
                    .replace("%amount%",String.valueOf(outtings.get(p) * bets.get(p))));
        }
    }

    public double getResult(){
        return 1.0+Math.log(1-Math.random())/(-Settings.DIFFICULTY_SCALING.getDouble());
    }




}



