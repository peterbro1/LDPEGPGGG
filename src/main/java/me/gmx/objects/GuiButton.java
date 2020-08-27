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

import me.gmx.config.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiButton {

    public ItemStack stack;
    public int loc;
    private String ID;
    public GuiButton(ItemStack stack, int location){
        this.stack = stack;
        this.loc = location;
    }
    public GuiButton(String ID,Material type, String name, int location, String... lore){
        this.stack = new ItemStack(type);
        this.loc = location;
        setLore(lore);
        this.ID = ID;
        setTitle(name);
    }

    public GuiButton(String ID, Material type, String name, int location, List<String> lore) {
        this.stack = new ItemStack(type);
        this.loc = location;
        this.ID = ID;
        setLore(lore);
        setTitle(name);
    }

    public void setMeta(ItemMeta meta){
        this.stack.setItemMeta(meta);
    }
    public void setTitle(String s){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Lang.fixColors(s));
        setMeta(meta);
    }
    public void setLore(String... s){
        ItemMeta meta = stack.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        for (String str : s){
            lore.add(Lang.fixColors(str));
        }
        meta.setLore(lore);
        setMeta(meta);
    }
    public void setLore(List<String> s){
        ItemMeta meta = stack.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        for (String str : s){
            lore.add(Lang.fixColors(str));
        }
        meta.setLore(lore);
        setMeta(meta);
    }

    public int getSlot(){
        return loc;
    }

    public String getID(){
        return this.ID;
    }


    public void executeAction(GuiMenu menu, Player clicker){

    }

    public List<String> getLore(){
        return this.stack.getItemMeta().getLore();
    }




}
