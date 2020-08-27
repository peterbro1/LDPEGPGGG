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
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class GuiMenu implements Listener {
    private Inventory inv;
    private ArrayList<GuiButton> buttons;
    public GuiMenu(int size){
         new GuiMenu(size,null);
    }

    public GuiMenu(int size, String name){
        if (size > 8 && size % 9 == 0)
            size /= 9;
        inv = Bukkit.createInventory(null,size*9,name);
        Bukkit.getPluginManager().registerEvents(this, Bustabit.getInstance());
        buttons = new ArrayList<GuiButton>();
    }

    public void addButton(GuiButton button){
        inv.setItem(button.loc,button.stack);
        Iterator<GuiButton> iter = buttons.iterator();
        while(iter.hasNext()){
            GuiButton temp = iter.next();
            if (temp.getSlot() == button.getSlot())
                iter.remove();
        }
        buttons.add(button);
    }

    public void changeButtonLore(GuiButton button, String... lore){
        if (buttons.contains(button)){
            ItemMeta meta = getInventory().getItem(button.getSlot()).getItemMeta();
            ArrayList<String> l = new ArrayList<String>();
            for(String string : lore)
                l.add(Lang.fixColors(string));
            meta.setLore(l);
            ItemStack stack = button.stack;
            stack.setItemMeta(meta);
            getInventory().setItem(button.getSlot(),stack);
            //this.getInventory().setItem(button.getSlot(),button.stack);
            for (HumanEntity p : getInventory().getViewers()){
                ((Player)p).updateInventory();
            }
        }
    }

    public void changeButtonText(GuiButton button, String name){
        if (buttons.contains(button)){
            ItemStack a = button.stack;
            ItemMeta meta = a.getItemMeta();
            meta.setDisplayName(Lang.fixColors(name));
            a.setItemMeta(meta);
            getInventory().setItem(button.getSlot(),a);
        }
    }

    public void setBorder(int width,ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Lang.LANG_GUI_BORDER.toString());
        stack.setItemMeta(meta);
        for (int x = 0; x <9; x++){
            for(int y = 0; y < inv.getSize()/9;y++)
                if (x == 0 || y == 0 || x == 8 || y == inv.getSize()/9-1){
                    inv.setItem(x+(y*9), stack);

                }
        }
    }

    public void addPlayers(HashMap<Player,Long> players){
        int slot = 11;
        clearPlayers();
        for (Player p : players.keySet()) {
            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1,(byte)3);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
            meta.setDisplayName(ChatColor.GREEN + p.getName());
            meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + ""+ players.get(p)));
            stack.setItemMeta(meta);
            while(inv.getItem(slot) != null){
                slot++;
                if (slot > 100)break;
            }
            inv.setItem(slot,stack);

        }
    }

    public void removePlayer(Player players){
        int slot = 11;

    }

    public void clearPlayers(){
        int slot = 11;
        while (slot < getInventory().getSize()) {
            if (inv.getItem(slot) != null)
                if (inv.getItem(slot).getType().equals(Material.SKULL_ITEM))
                    inv.setItem(slot, null);

                slot++;
        }

    }

    public boolean isPlayersFull(){
        return inv.getItem(43) == null;
    }

    public void open(Player p){
        p.openInventory(inv);
    }

    public Inventory getInventory(){
        return this.inv;
    }


    @EventHandler
    public void InventoryClick(InventoryClickEvent e){
        if (e.getInventory().equals(this.inv))
            e.setCancelled(true);
        else return;
        if (e.getClickedInventory() == null) return;
        if (e.getClick() != ClickType.DROP && e.getClickedInventory().equals(this.inv))
        for (GuiButton b : buttons){
            if (e.getSlot() == b.loc){
                b.executeAction(this,(Player)e.getWhoClicked());
                return;
            }


        }



    }

}
