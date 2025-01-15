package plugin.superplugin.supers.superkiwi;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.superplugin.CoolTimeManager;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.Function;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.customentity.Tornado;
import plugin.superplugin.customentity.WormHole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SuperKiwiFunction {
    private static String supername = "superkiwi";
    public static HashMap<UUID, Double> entityWormHoleCoolTime = new HashMap<>();

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
            if (36 - playerItemCount >= SuperKiwiItem.addItems.length) {
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
        playerInventory.addItem(SuperKiwiItem.addItems);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        player.sendMessage("변신!");
    }

    public static void Untransformed(Player player) {
        Inventory playerInventory = player.getInventory();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        playerData.remove(CustomKeys.Player_Super);
        for (int i = 0; i < SuperKiwiItem.removeItems.length; i++) {
            int[] indexes = Function.FindItemPDIndexesAtPlayerInventory(player, SuperKiwiItem.removeItems[i], PersistentDataType.INTEGER);
            for (int index : indexes) {
                if (playerInventory.getItem(index) == null)
                    continue;
                playerInventory.remove(playerInventory.getItem(index));
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            for (int i = 0; i < SuperKiwiItem.removeItems.length; i++) {
                if (Function.CompareItemPersistentData(player.getInventory().getItemInOffHand(), SuperKiwiItem.removeItems[i], PersistentDataType.INTEGER)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        player.sendMessage("변신 해제!");
    }

    public static void KiwiSkill_1(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            Inventory playerInventory = player.getInventory();
            for (int i = 0; i < playerInventory.getSize(); i++) {
                if (playerInventory.getItem(i) == null)
                    continue;

                if (playerInventory.getItem(i).equals(SuperKiwiItem.SUPER_KIWI_SKILL_1)) {
                    playerInventory.setItem(i, SuperKiwiItem.WORMHOLE);
                    break;
                }
            }
        }
    }
    public static void KiwiSkill_2(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            World world = player.getWorld();
            Block targetBlock = player.getTargetBlockExact(15);
            if (targetBlock != null) {
                Location targetLoc = targetBlock.getLocation().add(0, 1, 0);

                new BukkitRunnable() {
                    int timer = 0;
                    final int particleTime = 20 * 2;
                    final int spinCount = particleTime / 20;
                    final int radius = 5;
                    final int addYVelocity = 2;
                    final PotionEffect DARKNESS = new PotionEffect(PotionEffectType.DARKNESS, 3 * 20, 0, false, false, false);
                    final PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 1, false, false, false);

                    @Override
                    public void run() {
                        if (timer >= particleTime) {
                            cancel();

                            ArrayList<LivingEntity> entities = new ArrayList<>(targetLoc.getNearbyLivingEntities(radius));
                            for (LivingEntity entity : entities) {
                                Vector velocity = entity.getVelocity().clone();
                                velocity.setY(velocity.getY() + addYVelocity);
                                entity.setVelocity(velocity);
                                entity.addPotionEffect(DARKNESS);
                                entity.addPotionEffect(SLOWNESS);
                                world.spawnParticle(Particle.GUST, entity.getLocation(), 5, 1, 1, 1);
                            }
                        }

                        double degree = ((double) timer / particleTime) * spinCount * 360;
                        Location particleLoc = Function.getCircleLocation(radius, degree, targetLoc);
                        particleLoc = Function.GetHighestLocNear(particleLoc, 3);
                        if (particleLoc != null) {
                            particleLoc.add(0, 1, 0);
                            world.spawnParticle(Particle.SMALL_GUST, particleLoc, 10, 0.2, 0.2, 0.2, 0);
                        }

                        timer++;
                        CoolTimeManager.SetCoolTime(player, supername, 2, delay);
                    }
                }.runTaskTimer(SuperPlugin.getInstance(), 0, 1);
            }
        }
    }
    public static void KiwiSkill_3(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            final PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 30, 2, false, false, false);
            player.addPotionEffect(SLOWNESS);
            World world = player.getWorld();

            CoolTimeManager.SetCoolTime(player, supername, 3, delay);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Vector dir = player.getLocation().getDirection().normalize();
                    dir.setY(dir.getY() + 0.1);
                    dir.multiply(3);
                    player.setVelocity(dir);

                    world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    world.spawnParticle(Particle.EXPLOSION, player.getLocation(), 10, 1, 1, 1, 0);

                    new BukkitRunnable() {
                        int timer = 0;
                        ArrayList<UUID> damagedEntity = new ArrayList<>();

                        @Override
                        public void run() {
                            Location playerLoc = player.getLocation();

                            if ((playerLoc.clone().add(0, -0.1, 0).getBlock().getType() != Material.AIR && timer > 5) || timer >= 10 * 20) {
                                cancel();
                            }

                            ArrayList<LivingEntity> nearbyLivingEntities = new ArrayList<>(playerLoc.getNearbyLivingEntities(3));
                            for (LivingEntity entity : nearbyLivingEntities) {
                                if (entity.getUniqueId() == player.getUniqueId() || damagedEntity.contains(entity.getUniqueId()))
                                    continue;

                                entity.damage(4);
                                damagedEntity.add(entity.getUniqueId());
                            }

                            world.spawnParticle(Particle.SONIC_BOOM, playerLoc, 1, 0, 0, 0, 0);
                            CoolTimeManager.SetCoolTime(player, supername, 3, delay);

                            timer++;
                        }
                    }.runTaskTimer(SuperPlugin.getInstance(), 0, 2);
                }
            }.runTaskLater(SuperPlugin.getInstance(), 30);
        }
    }

    public static void KiwiUltimate(Player player, int delay) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if (Objects.equals(playerData.get(CustomKeys.Player_Super, PersistentDataType.STRING), supername)) {
            new Tornado(player, player.getLocation(), 10, delay);
        }
    }
}
