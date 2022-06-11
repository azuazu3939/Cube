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

                    ArmorStand stand1 = (ArmorStand) event.getPlayer().getVehicle();

                    if (event.getPlayer() != null || event.getPlayer().getVehicle() == stand1) {

                        if(stand1.getPassengers().isEmpty()) return;

                        Vector locVec = stand1.getVelocity();

                        Vector vector0 = event.getPlayer().getVelocity().getMidpoint(new Vector()).multiply(100);
                        Vector vector1 = event.getPlayer().getVelocity().getMidpoint(new Vector()).getMidpoint(vector0);
                        Vector vector2 = event.getPlayer().getVelocity().getMidpoint(new Vector().setY(0.12).setX(0).setZ(0)).multiply(100);
                        Vector vector3 = event.getPlayer().getVelocity().getMidpoint(new Vector().setY(-0.4));

                        float loc1 = event.getPlayer().getLocation().getYaw();
                        float loc2 = event.getPlayer().getLocation().getPitch();

                        if (ppisv.c() > 0.1) {
                            Objects.requireNonNull(stand1).setVelocity((vector0));
                            Objects.requireNonNull(stand1).setRotation(loc1, loc2);
                        }
                        if (ppisv.c() < -0.1) {
                            Objects.requireNonNull(stand1).setVelocity(vector1);
                            Objects.requireNonNull(stand1).setRotation(loc1, loc2);
                        }
                        if (ppisv.d()) {
                            if (Objects.requireNonNull(stand1).isOnGround()) {
                                Objects.requireNonNull(stand1).setVelocity(vector2);
                                Objects.requireNonNull(stand1).setRotation(loc1, loc2);
                            }
                        }
                        if (!Objects.requireNonNull(stand1).isOnGround()) {
                            Objects.requireNonNull(stand1).setVelocity(vector3);
                            Objects.requireNonNull(stand1).setRotation(loc1, loc2);
                        }
                        if (ppisv.e()) stand1.setSmall(false);
                    }
                }
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        ArmorStand stand = (ArmorStand) event.getEntity();
        stand.setSmall(true);
        stand.addPassenger(player);
        event.setCancelled(true);
    }
}


