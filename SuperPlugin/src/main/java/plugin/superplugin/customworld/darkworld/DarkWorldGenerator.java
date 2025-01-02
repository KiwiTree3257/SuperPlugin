package plugin.superplugin.customworld.darkworld;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class DarkWorldGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        ChunkData chunkData = createChunkData(world); // ChunkData 객체 생성
        Random rand = new Random();

        // 블록 설정: 16x16 청크의 각 좌표를 설정
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 91, z, Material.BEDROCK);
                chunkData.setBlock(x, 90, z, Material.STONE_BRICKS);

                chunkData.setBlock(x, 60, z, Material.STONE_BRICKS);
                chunkData.setBlock(x, 59, z, Material.BEDROCK);
            }
        }

        if (rand.nextInt(2) == 0) {
            chunkData.setBlock(rand.nextInt(16), 61, rand.nextInt(16), Material.SOUL_LANTERN);
        }

        if (rand.nextInt(3) == 0) {
            for (int x = 2; x < 14; x++) {// 기둥
                for (int z = 2; z < 14; z++) {
                    for (int y = 60; y < 90; y++) {
                        chunkData.setBlock(x, y, z, Material.STONE_BRICKS);
                    }
                }
            }
        }

        return chunkData; // 완성된 청크 데이터 반환
    }

    public static World CreateDarkWorld(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        World world;

        worldCreator.generator(new DarkWorldGenerator());
        world = worldCreator.createWorld();

        if (world != null) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);

            world.setTime(18000);
            world.setSpawnLocation(0, 61, 0);
        }

        return world;
    }
}
