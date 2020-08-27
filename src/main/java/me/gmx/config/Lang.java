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
package me.gmx.config;

import me.gmx.core.BConfig;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;


public enum Lang {

    PREFIX("&d&lCRASH!&r "),
    LANG_CONSOLE("&4[&cServer&4]&r "),
    MSG_NOACCESS("&4Insufficient permissions!"),
    BUTTON_LORE("&4Click here to place a bet!/&2You can only place bets between games"),
    MSG_EXPIRED("&4Your betting session has ended because the game has started"),
    MSG_PLAYERONLY("This command can only be used by players"),
    MSG_USAGE_SUBCOMMAND("&4Incorrect usage! Please refer to %usage%"),
    BUTTON_NAME("&d&lCRASH!"),
    HISTORY_MULTIPLIER_TEXT("%multiplier%"),
    PREVIOUS_CRASHES_NAME("&2&lPrevious crashes"),
    BUTTON_NAME_RUNNING("&d&lCurrent multiplier:"),
    MSG_TYPE_AMOUNT("&4Please type the amount you wish to bet. Do not include any symbols"),
    MSG_ERROR("Error occured, please contact server developer."),
    LANG_GUI_BORDER("&dCRASH!"),
    BOOK_TITLE("&4Book"),
    BOOK_LORE("Book/Book"),
    MSG_WINNER("&2&lYOU HAVE JUST WON $%amount%!"),
    BROADCAST_MSG("%player% has just cashed out at %multiplier% for a total winning of %win%! WOW!"),
    MSG_GAME_FULL("&4This game is currently full! Try again later"),
    BUTTON_INTERM("Game is not currently running. Click here to place a bet!"),
    MSG_INVALID_AMT("&4You have entered an invalid amount. Do not use any symbols or commas"),
    MSG_INSUFF_AMT("&4You do not have enough money to place this bet!"),
    MSG_BET_SUCCESS("&2You have placed your bet of %bet%"),
    MSG_LOSE("&4You've lost %bet%"),
    GUI_TITLE("&4Crash!");


    private String defaultValue;
    private static BConfig config;

    Lang(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }

    public String toString() { return fixColors(config.getConfig().getString(getPath())); }

    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }

    public List<String> getStringList(){
        return Arrays.asList(toString().split("/"));
    }

    public String toMsg() {
        boolean bool = true;
        if (bool) {
            return fixColors(config.getConfig().getString(PREFIX.getPath()) + config.getConfig()
                    .getString(getPath()));
        }
        return fixColors(config.getConfig().getString(getPath()));
    }

    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Lang lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }


    public static String fixColors(String paramString) {
        if (paramString == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }
}
