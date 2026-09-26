package com.brandon3055.draconicevolution.common.tileentities.multiblocktiles.reactor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.brandon3055.draconicevolution.common.utils.WorldHeight;
import com.brandon3055.draconicevolution.common.utils.handlers.IProcess;

/**
 * Created by brandon3055 on 12/8/2015.
 */
public class ReactorExplosionTrace implements IProcess {

    private final World world;
    private final int xCoord;
    private final int yCoord;
    private final int zCoord;
    private final float power;
    private boolean isDead = false;

    public ReactorExplosionTrace(World world, int x, int y, int z, float power) {
        this.world = world;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.power = power;
    }

    @Override
    public void updateProcess() {

        float energy = power * 10;
        int minY = WorldHeight.getMinHeight(world, 1);
        int maxY = WorldHeight.getMaxHeight(world, 255);

        for (long traceY = yCoord; traceY >= minY && energy > 0; traceY--) {
            int y = (int) traceY;
            Block block = world.getBlock(xCoord, y, zCoord);

            List<Entity> entities = world.getEntitiesWithinAABB(
                    Entity.class,
                    AxisAlignedBB.getBoundingBox(xCoord, y, zCoord, xCoord + 1, traceY + 1, zCoord + 1));
            for (Entity entity : entities) {
                entity.attackEntityFrom(ReactorExplosion.FUSION_EXPLOSION, power * 100);
            }

            energy -= block instanceof BlockLiquid ? 10 : block.getExplosionResistance(null);

            boolean blockRemoved = false;
            if (energy >= 0 && block != Blocks.air) {
                world.setBlockToAir(xCoord, y, zCoord);
                blockRemoved = true;
            }
            energy -= 0.5F + 0.1F * (yCoord - traceY);

            if (energy <= 0 && world.rand.nextInt(20) == 0 && blockRemoved) {
                if (world.rand.nextInt(3) > 0) {
                    world.setBlock(xCoord, y, zCoord, Blocks.fire);
                } else {
                    world.setBlock(xCoord, y, zCoord, Blocks.flowing_lava);
                }
            }
        }

        energy = power * 20;
        for (long traceY = (long) yCoord + 1; traceY < maxY && energy > 0; traceY++) {
            int y = (int) traceY;
            Block block = world.getBlock(xCoord, y, zCoord);

            List<Entity> entities = world.getEntitiesWithinAABB(
                    Entity.class,
                    AxisAlignedBB.getBoundingBox(xCoord, y, zCoord, xCoord + 1, traceY + 1, zCoord + 1));
            for (Entity entity : entities) {
                entity.attackEntityFrom(ReactorExplosion.FUSION_EXPLOSION, power * 100);
            }

            energy -= block instanceof BlockLiquid ? 10 : block.getExplosionResistance(null);
            if (energy >= 0) {
                world.setBlockToAir(xCoord, y, zCoord);
            }
            energy -= 0.5F + (0.1F * (traceY - yCoord));
        }

        isDead = true;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }
}
