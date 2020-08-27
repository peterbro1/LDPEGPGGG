package me.gmx;

import me.gmx.command.CmdCrash;
import me.gmx.config.Lang;
import me.gmx.config.Settings;
import me.gmx.core.BConfig;
import me.gmx.objects.ChatHandler;
import me.gmx.objects.GameGui;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bustabit extends JavaPlugin {

    private static Bustabit ins;
    Logger logger;
    public BConfig langConfig;
    public BConfig mainConfig;
    public Economy econ = null;
    public ChatHandler chand;
    public GameGui gui;

    public void onEnable(){
        ins = this;
        logger = getLogger();
        chand = new ChatHandler(getInstance());
        this.langConfig = new BConfig(this,"Lang.yml");
        this.mainConfig = new BConfig(this,"Settings.yml");
        Lang.setConfig(this.langConfig);
        Settings.setConfig(this.mainConfig);
        registerCommands();
        gui = new GameGui(getInstance());
        if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //registerListeners();

    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Bustabit getInstance(){
        return ins;
    }

    public void reloadConfig() {
        this.langConfig = new BConfig(this,"Lang.yml");
        Lang.setConfig(this.langConfig);
        this.mainConfig = new BConfig(this,"Settings.yml");
        Settings.setConfig(this.mainConfig);

    }
    public void log(String message){
        logger.log(Level.INFO,"["+getDescription().getName()+"] " + message);
    }

    private void registerCommands(){
        getCommand("bustabit").setExecutor(new CmdCrash(getInstance()));
    }



}
