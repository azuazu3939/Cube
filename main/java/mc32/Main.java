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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        this.getCommand("CubeCall").setExecutor(new CubeCommandExcutor());
        manager.addPacketListener(new PacketAdapter(this, PacketType.Play.Client.STEER_VEHICLE) {


            @Override
            public void onPacketReceiving(PacketEvent event) {

                PacketContainer packet = event.getPacket();
                PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet.getHandle();

                if (event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) {

                    Player player = event.getPlayer();
                    Entity entity = event.getPlayer().getVehicle();

                    if (!(entity instanceof ArmorStand)) return;
                    if (player == null) return;

                    ArmorStand stand1 = (ArmorStand) event.getPlayer().getVehicle();

                    if (event.getPlayer() != null || event.getPlayer().getVehicle() == stand1) {

                        int addspeed = 0;
                        double addjump = 0;
                        int addslow = 0;

                        if (stand1.getPassengers().isEmpty()) return;
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) addspeed = 5 * Objects.requireNonNull(player.getPotionEffect(PotionEffectType.SPEED)).getAmplifier();
                        if (player.hasPotionEffect(PotionEffectType.JUMP)) addjump = Objects.requireNonNull(player.getPotionEffect(PotionEffectType.JUMP)).getAmplifier();
                        if (player.hasPotionEffect(PotionEffectType.SLOW)) addslow = 15 * Objects.requireNonNull(player.getPotionEffect(PotionEffectType.SLOW)).getAmplifier();

                        Vector vector0 = new Vector().add(player.getVelocity()).clone().multiply((75 + addspeed * 3) - addslow);
                        Vector vector1 = vector0.getMidpoint(player.getVelocity());

                        Vector vector2 = new Vector().add(player.getVelocity()).clone().multiply(150).setY(0.6 + addjump / 8);
                        Vector vector3 = new Vector().add(player.getVelocity()).setY(-1);
                        Vector vector4 = stand1.getVelocity().multiply(0.95);

                        float loc1 = event.getPlayer().getLocation().getYaw();
                        float loc2 = event.getPlayer().getLocation().getPitch();

                        boolean jump = false;

                        if (ppisv.c() > 0.1) {
                            stand1.setVelocity(vector0);
                            stand1.setRotation(loc1, loc2);
                        }
                        if (ppisv.c() < -0.1) {
                            stand1.setVelocity(vector1);
                            stand1.setRotation(loc1, loc2);
                        }
                        if (ppisv.d() && stand1.isOnGround()) {
                            stand1.setVelocity(vector2);
                            stand1.setRotation(loc1, loc2);
                            jump = true;
                        }
                        if (!stand1.isOnGround()) {
                            stand1.setVelocity(vector3);

                            if (ppisv.c() > 0.1) stand1.setVelocity(vector4);
                            if (ppisv.d() && jump) stand1.setVelocity(vector2);

                            stand1.setRotation(loc1, loc2);
                        }
                        if (ppisv.e()) {
                            stand1.setSmall(false);
                        }

                    }
                }
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        Entity entity1 = event.getEntity();
        if (entity instanceof Player && entity1 instanceof ArmorStand) {
            ArmorStand stand = (ArmorStand) event.getEntity();
            stand.setSmall(true);
            stand.setCollidable(false);
            stand.addPassenger(entity);
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        ArmorStand stand = (ArmorStand) event.getDismounted();
        stand.remove();
    }
}


