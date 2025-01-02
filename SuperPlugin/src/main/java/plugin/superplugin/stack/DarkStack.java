package plugin.superplugin.stack;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.bossbar.DarkBossBar;
import plugin.superplugin.bossbar.FreezeBossBar;

import java.util.HashMap;
import java.util.Objects;

public class DarkStack {
    private static HashMap<Player, DarkBossBar> darkBossBars = new HashMap<>();

    public static void DarkEntity(LivingEntity entity) {
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        if (entity instanceof Player &&
                Objects.equals(entity.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), "supersihoo")) {
            return;
        }

        if (entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) == null) {//스택 추가
            entityData.set(CustomKeys.DARK_STACK, PersistentDataType.INTEGER, 0);

            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (!darkBossBars.containsKey(player)) {//보스바 추가
                    DarkBossBar darkBossBar = new DarkBossBar();
                    darkBossBars.put(player, darkBossBar);

                    darkBossBar.addPlayer(player);
                    darkBossBar.updateProgress(0);
                }
            }
        }

        if (entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) != null) {//스택 증가
            entityData.set(CustomKeys.DARK_STACK, PersistentDataType.INTEGER, entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) + 1);

            DarkTime(entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER), entity);

            if (entity instanceof Player) {//보스바 증가
                Player player = (Player) entity;

                if (darkBossBars.containsKey(player)) {
                    darkBossBars.get(player).updateProgress(entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER).floatValue() / 6f);
                }
            }

            if (entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) >= 6) {//스택 끝
                int effectTime = 5 * 20;
                PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, effectTime, 1, false, false, false);
                PotionEffect BLINDNESS = new PotionEffect(PotionEffectType.BLINDNESS, effectTime, 0, false, false, false);
                PotionEffect WITHER = new PotionEffect(PotionEffectType.WITHER, effectTime, 2, false, false, false);
                entity.addPotionEffect(SLOWNESS);
                entity.addPotionEffect(BLINDNESS);
                entity.addPotionEffect(WITHER);
                entityData.remove(CustomKeys.DARK_STACK);

                if (entity instanceof Player) {//보스바 삭제
                    Player player = (Player) entity;

                    if (darkBossBars.containsKey(player)) {
                        darkBossBars.get(player).removeBossBar();
                        darkBossBars.remove(player);
                    }
                }
            }
        }
    }

    private static void DarkTime(int beforeValue, Entity entity) {
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        new BukkitRunnable() {//보스바 감소
            @Override
            public void run() {
                if (entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) != null) {
                    if (beforeValue != entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER)) {
                        cancel();
                        return;
                    }

                    if (entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) == 1) {
                        entityData.remove(CustomKeys.DARK_STACK);

                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            if (darkBossBars.containsKey(player)) {
                                darkBossBars.get(player).removeBossBar();
                                darkBossBars.remove(player);
                            }
                        }
                    }
                    else {
                        entityData.set(CustomKeys.DARK_STACK, PersistentDataType.INTEGER, entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER) - 1);

                        DarkTime(entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER), entity);

                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            if (darkBossBars.containsKey(player)) {
                                darkBossBars.get(player).updateProgress(entityData.get(CustomKeys.DARK_STACK, PersistentDataType.INTEGER).floatValue() / 6f);
                            }
                        }
                    }
                }
            }
        }.runTaskLater(SuperPlugin.getInstance(), 2 * 20);
    }
}
