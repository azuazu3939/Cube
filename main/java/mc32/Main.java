package main.java.mc32;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, PacketType.Play.Client.STEER_VEHICLE) {


            @Override
            public void onPacketReceiving(PacketEvent event) {

                PacketContainer packet = event.getPacket();
                PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet.getHandle();

                if (event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) {

                    Entity entity = Objects.requireNonNull(event.getPlayer().getVehicle());

                    if (!(entity instanceof ArmorStand)) return;

                    ArmorStand stand = (ArmorStand) event.getPlayer().getVehicle();

                    if (event.getPlayer() != null || event.getPlayer().getVehicle() == stand) {

                        Vector vector0 = event.getPlayer().getLocation().getDirection().multiply(0.9);
                        Vector vector1 = event.getPlayer().getEyeLocation().getDirection().multiply(-0.9);
                        Vector vector2 = event.getPlayer().getLocation().getDirection().setY(1);
                        Vector vector3 = event.getPlayer().getLocation().getDirection().setY(-1);

                        if (ppisv.c() > 0.5) {
                            Objects.requireNonNull(stand).setVelocity(vector0);
                        }
                        if (ppisv.c() < -0.5) {
                            Objects.requireNonNull(stand).setVelocity(vector1);
                        }
                        if (ppisv.d()) {
                            if (Objects.requireNonNull(stand).isOnGround())
                                Objects.requireNonNull(stand).setVelocity(vector2);
                        }
                        if (!Objects.requireNonNull(stand).isOnGround())
                            Objects.requireNonNull(stand).setVelocity(vector3);
                    }
                }
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        ArmorStand stand = (ArmorStand) event.getEntity();
        stand.addPassenger(player);
        event.setCancelled(true);
    }
}


