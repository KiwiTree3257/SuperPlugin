package plugin.superplugin.customentity;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.supers.superkiwi.SuperKiwiFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class WormHole {
    private OneWormHole[] wormHoles = new OneWormHole[2];
    private UUID playerUUID;

    public WormHole(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void ThrowWormHole(int throwCount) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return;
        }

        int beforeIndex = (throwCount > 0 ? throwCount : wormHoles.length) - 1;
        if (wormHoles[throwCount] != null) {
            wormHoles[throwCount].Close();
        }
        wormHoles[throwCount] = new OneWormHole(player.getLocation().clone().add(0, 1.5, 0), player.getLocation().getDirection(), throwCount);
        if (wormHoles[beforeIndex] != null) {
            wormHoles[throwCount].wormHoleOther = wormHoles[beforeIndex];
            wormHoles[beforeIndex].wormHoleOther = wormHoles[throwCount];
        }
    }

    private class OneWormHole {
        private Particle.DustOptions AQUADUST = new Particle.DustOptions(Color.AQUA, 1.5f);
        private Particle.DustOptions PURPLEDUST = new Particle.DustOptions(Color.PURPLE, 1.5f);
        private Location wormHoleLoc;
        public OneWormHole wormHoleOther;
        public boolean open = false;
        public boolean close = false;

        public OneWormHole(Location spawnLoc, Vector moveDir, int wormHoleKind) {
            wormHoleLoc = spawnLoc.clone();
            World world = spawnLoc.getWorld();

            new BukkitRunnable() {
                int timer = 0;
                Location beforeLoc = wormHoleLoc.clone();

                @Override
                public void run() {
                    if (timer >= 2 * 20 || close) {
                        wormHoleLoc.add(0, -1, 0);

                        if (timer >= 10 * 20 || close) {
                            cancel();
                        }
                    }
                    else {
                        beforeLoc = wormHoleLoc.clone();
                        wormHoleLoc.add(moveDir);
                    }

                    if (Function.GetIsCollision(wormHoleLoc, beforeLoc, 0.1)) {
                        cancel();

                        open = true;
                        new BukkitRunnable() {
                            int timer_2 = 0;
                            int circlePoints = 40;
                            int radius = 2;

                            Location highestLoc = Function.GetHighestLocNear(wormHoleLoc, 2);
                            @Override
                            public void run() {
                                if (timer_2 >= 10 * 20 || close) {
                                    open = false;
                                    cancel();
                                }

                                if (highestLoc != null) {
                                    for (int i = 0; i < circlePoints; i++) {
                                        Location particleLoc = Function.getCircleLocation(radius, i * ((double) 360 / circlePoints), highestLoc);
                                        particleLoc.add(0, 1, 0);

                                        switch (wormHoleKind) {
                                            case 0 -> world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, AQUADUST);
                                            case 1 -> world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, PURPLEDUST);
                                        }
                                    }//particle
                                }

                                if (wormHoleOther != null) {
                                    ArrayList<LivingEntity> entities = new ArrayList<>(wormHoleLoc.getNearbyLivingEntities(radius));
                                    if (!entities.isEmpty()) {
                                        Location tpLoc = wormHoleOther.GetTPLocation();
                                        LivingEntity entity = entities.getFirst();
                                        SuperKiwiFunction.entityWormHoleCoolTime.putIfAbsent(entity.getUniqueId(), 0.0);
                                        Double coolTime = SuperKiwiFunction.entityWormHoleCoolTime.get(entity.getUniqueId());
                                        if (tpLoc != null && wormHoleOther.open && coolTime < System.currentTimeMillis()) {
                                            world.playSound(entity.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
                                            world.spawnParticle(Particle.ENCHANT, tpLoc, 200, 0, 0, 0, 1);

                                            tpLoc.setYaw(entity.getYaw());
                                            tpLoc.setPitch(entity.getPitch());
                                            entity.setFallDistance(0);
                                            entity.teleport(tpLoc);
                                            world.playSound(entity.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
                                            world.spawnParticle(Particle.ENCHANT, tpLoc, 200, 0, 0, 0, 1);

                                            SuperKiwiFunction.entityWormHoleCoolTime.put(entity.getUniqueId(), (double) (System.currentTimeMillis() + 2000));/////////////////
                                        }
                                    }
                                }

                                timer_2++;
                            }
                        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
                    }

                    switch (wormHoleKind) {
                        case 0 -> world.spawnParticle(Particle.DUST, wormHoleLoc, 10, 0.1, 0.1, 0.1, 0.1, AQUADUST);
                        case 1 -> world.spawnParticle(Particle.DUST, wormHoleLoc, 10, 0.1, 0.1, 0.1, 0.1, PURPLEDUST);
                    }
                    timer++;
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }

        public Location GetTPLocation() {
            Location tpLoc = Function.GetHighestLocNear(wormHoleLoc, 3);

            if (open && tpLoc != null) {
                return tpLoc.add(0, 1, 0);
            }

            return null;
        }

        public void Close() {
            close = true;
        }
    }
}
