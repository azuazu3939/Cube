package main.java.mc32;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;

public class CubeCommandExcutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String playerName;
        try {
            playerName = args[0];
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "正常な引数を渡してください。");
            return true;
        }
        Player player;
        try {
            player = getPlayer(playerName);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "そのプレイヤーは現在いません。");
                return true;
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "そのプレイヤーは現在いません。");
            return true;
        }
        Location loc;
        try {
            loc = player.getLocation();
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "正常な位置にいません。");
            return true;
        }
        World world;
        try {
            world = player.getWorld();
            ArmorStand stand = (ArmorStand) Objects.requireNonNull(world).spawnEntity(loc, EntityType.ARMOR_STAND);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "エラーが発生しました。");
            return true;
        }
        return true;
    }
}
