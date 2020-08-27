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

import me.gmx.config.Settings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BGame {

    private int max;
    private ArrayList<Player> players;
    public BGame(int max, ArrayList<Player> players){
        this.max = max;
        this.players = players;
    }

    public BGame(){}


    public double getResult(){
       return Math.log(1-Math.random())/(-Settings.DIFFICULTY_SCALING.getDouble());
    }
}
