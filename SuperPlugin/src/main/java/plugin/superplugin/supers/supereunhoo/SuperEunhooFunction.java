package plugin.superplugin.supers.supereunhoo;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.customentity.DarkGrab;
import plugin.superplugin.stack.DarkStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SuperEunhooFunction {
    private static String supername = "supereunhoo";
    private static HashMap<UUID, DarkGrab> playersDarkGrab = new HashMap<>();

    public static void SuperTransformation(Player player) {
        Inventory playerInventory = player.getInventory();
        ItemStack[] playerItemStacks = playerInventory.getStorageContents();
        int playerItemCount = 0;

        for (int i = 0; i < playerItemStacks.length; i++) {
            if (playerItemStacks[i] != null)
                playerItemCount++;
        }
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (playerData.has(CustomKeys.Player_Super)) {
            if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
                Untransformed(player);
            }
            else {
                player.sendMessage("변신 실패.");
            }
        }
        else {
            if (36 - playerItemCount >= SuperEunhooItem.addItems.length) {
                playerData.set(CustomKeys.Player_Super, PersistentDataType.STRING, supername);
                Transformation(player);
            }
            else {
                player.sendMessage("변신을 위한 인벤토리 공간이 부족합니다.");
            }
        }
    }

    public static void Transformation(Player player) {
        Inventory playerInventory = player.getInventory();
        playerInventory.addItem(SuperEunhooItem.addItems);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        player.sendMessage("변신!");
    }

    public static void Untransformed(Player player) {
        Inventory playerInventory = player.getInventory();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        playerData.remove(CustomKeys.Player_Super);
        for (int i = 0; i < SuperEunhooItem.removeItems.length; i++) {
            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperEunhooItem.removeItems[i], PersistentDataType.INTEGER);
            for (int index : indexes) {
                if (playerInventory.getItem(index) == null)
                    continue;
                playerInventory.remove(playerInventory.getItem(index));
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            for (int i = 0; i < SuperEunhooItem.removeItems.length; i++) {
                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), SuperEunhooItem.removeItems[i], PersistentDataType.INTEGER)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        player.sendMessage("변신 해제!");
    }

    public static void EunhooSkill_1(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            playersDarkGrab.putIfAbsent(player.getUniqueId(), null);

            DarkGrab darkGrab = playersDarkGrab.get(player.getUniqueId());
            if (darkGrab != null) {
                //그랩이 있음

                if (darkGrab.isDead) {
                    //끝남
                    if (!CoolTimeManager.CheckCoolTime(player, supername, 1)) {
                        playersDarkGrab.put(player.getUniqueId(), new DarkGrab(player, delay));
                    } else {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 1) + "초 남음");
                    }
                }
                else if (darkGrab.getTargetLocation() != null) {
                    //적중
                    darkGrab.isDead = true;

                    new BukkitRunnable() {
                        int timer = 0;

                        @Override
                        public void run() {
                            if (timer >= 2 * 20 || darkGrab.getTargetLocation().distance(player.getLocation()) <= 2) {
                                player.setVelocity(new Vector(0, 0, 0));
                                cancel();
                                return;
                            }

                            Vector targetDir = darkGrab.getTargetLocation().subtract(player.getLocation()).toVector().normalize().multiply(2);
                            if (player.getLocation().clone().add(targetDir.normalize()).getBlock().isCollidable() && targetDir.getY() > 0) {
                                targetDir.setY(targetDir.getY() + 1);
                            }

                            player.setVelocity(targetDir);
                            player.setFallDistance(0);

                            timer++;
                        }
                    }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
                }
                else {
                    //진행중
                    if (CoolTimeManager.CheckCoolTime(player, supername, 1)) {
                        player.sendMessage("쿨타임 " + CoolTimeManager.GetCoolTime(player, supername, 1) + "초 남음");
                    }
                }
            }
            else {
                playersDarkGrab.put(player.getUniqueId(), new DarkGrab(player, delay));
            }
        }
    }
    public static void EunhooSkill_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            int radius = 4;
            int skillTime = 6 * 20;
            final Particle.DustOptions blackDust = new Particle.DustOptions(Color.BLACK, 2);
            final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 5, 2, false, false);

            new BukkitRunnable() {
                int timer = 1;

                @Override
                public void run() {
                    if (timer > skillTime) {
                        cancel();
                    }

                    Location center = player.getLocation();
                    World world = player.getWorld();

                    int circlePoints = 40;
                    for (int i = 0; i < circlePoints; i++) {
                        Location particleLoc = Function.getCircleLocation(radius, i * ((double) 360 / circlePoints), center);
                        particleLoc = Function.GetHighestLocNear(particleLoc, 2);
                        if (particleLoc == null) {
                            continue;
                        }
                        particleLoc.add(0, 1, 0);

                        if (timer % 40 == 0) {
                            world.spawnParticle(Particle.SOUL, particleLoc, 50, 0, 0, 0, 0.1);
                        }
                        else {
                            world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, blackDust);
                        }
                    }

                    if (timer % 40 == 0) {
                        ArrayList<LivingEntity> entities = new ArrayList<>(center.getNearbyLivingEntities(radius));
                        for (LivingEntity entity : entities) {
                            if (entity.getUniqueId().equals(player.getUniqueId())) {
                                continue;
                            }

                            world.spawnParticle(Particle.SQUID_INK, entity.getLocation(), 5, 0.5, 0.5, 0.5, 0);
                            DarkStack.DarkEntity(entity);
                            entity.getPersistentDataContainer().set(CustomKeys.MOVE_STOP, PersistentDataType.BOOLEAN, true);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (LivingEntity entity : entities) {
                                    entity.getPersistentDataContainer().remove(CustomKeys.MOVE_STOP);
                                }
                            }
                        }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
                    }

                    player.addPotionEffect(SPEED);
                    CoolTimeManager.SetCoolTime(player, supername, 2, delay);
                    timer++;
                }
            }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
        }
    }
    public static void EunhooSkill_3(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {

        }
    }

    public static void EunhooUltimate(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            World darkWorld = SuperPlugin.darkWorld;

            if (darkWorld != null) {
                Location darkWorldSpawnLoc = darkWorld.getSpawnLocation();

                new BukkitRunnable() {
                    int timer = 0;

                    @Override
                    public void run() {
                        if (timer >= 10 * 20) {
                            cancel();
                        }



                        timer++;
                    }
                }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
            }
        }
    }
}
