package plugin.superplugin.stack;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.superplugin.CustomKeys;
import plugin.superplugin.SuperPlugin;
import plugin.superplugin.bossbar.FreezeBossBar;

import java.util.HashMap;
import java.util.Objects;

public class FreezeStack {
    private static HashMap<Player, FreezeBossBar> freezeBossBars = new HashMap<>();

    public static void FreezeEntity(LivingEntity entity) {
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        if (entity instanceof Player &&
                Objects.equals(entity.getPersistentDataContainer().get(CustomKeys.Player_Super, PersistentDataType.STRING), "supersihoo")) {
            return;
        }

        if (entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) == null) {//스택 추가
            entityData.set(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER, 0);

            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (!freezeBossBars.containsKey(player)) {//보스바 추가
                    FreezeBossBar freezeBossBar = new FreezeBossBar();
                    freezeBossBars.put(player,freezeBossBar);

                    freezeBossBar.addPlayer(player);
                    freezeBossBar.updateProgress(0);
                }
            }
        }

        if (entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) != null) {//스택 증가
            entityData.set(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER, entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) + 1);

            FreezeTime(entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER), entity);

            if (entity instanceof Player) {//보스바 증가
                Player player = (Player) entity;

                if (freezeBossBars.containsKey(player)) {
                    freezeBossBars.get(player).updateProgress(entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER).floatValue() / 6f);
                }
            }

            if (entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) >= 6) {//스택 끝
                int effectTime = 5 * 20;
                PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, effectTime, 3, false, false, false);
                PotionEffect MINING_FATIGUE = new PotionEffect(PotionEffectType.MINING_FATIGUE, effectTime, 1, false, false, false);
                entity.addPotionEffect(SLOWNESS);
                entity.addPotionEffect(MINING_FATIGUE);
                entityData.remove(CustomKeys.FREEZE_STACK);

                entity.damage(4);
                if (entity instanceof Player) {//보스바 삭제
                    Player player = (Player) entity;

                    if (freezeBossBars.containsKey(player)) {
                        freezeBossBars.get(player).removeBossBar();
                        freezeBossBars.remove(player);
                    }
                }
            }
        }
    }

    private static void FreezeTime(int beforeValue, Entity entity) {
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        new BukkitRunnable() {//보스바 감소
            @Override
            public void run() {
                if (entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) != null) {
                    if (beforeValue != entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER)) {
                        cancel();
                        return;
                    }

                    if (entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) == 1) {
                        entityData.remove(CustomKeys.FREEZE_STACK);

                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            if (freezeBossBars.containsKey(player)) {
                                freezeBossBars.get(player).removeBossBar();
                                freezeBossBars.remove(player);
                            }
                        }
                    }
                    else {
                        entityData.set(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER, entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER) - 1);

                        FreezeTime(entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER), entity);

                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            if (freezeBossBars.containsKey(player)) {
                                freezeBossBars.get(player).updateProgress(entityData.get(CustomKeys.FREEZE_STACK, PersistentDataType.INTEGER).floatValue() / 6f);
                            }
                        }
                    }
                }
            }
        }.runTaskLater(SuperPlugin.getInstance(), 2 * 20);
    }
}
