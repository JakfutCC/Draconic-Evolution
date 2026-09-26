package com.brandon3055.draconicevolution.common.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.brandon3055.draconicevolution.common.tileentities.multiblocktiles.reactor.ReactorExplosion;
import com.brandon3055.draconicevolution.common.utils.WorldHeight;
import com.brandon3055.draconicevolution.common.utils.handlers.IProcess;

/**
 * Created by brandon3055 on 12/8/2015.
 */
public class ChaosImplosionTrace implements IProcess {

    private World worldObj;
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private float power;
    private Random random;

    public ChaosImplosionTrace(World world, int x, int y, int z, float power, Random random) {
        this.worldObj = world;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.power = power;
        this.random = random;
    }

    @Override
    public void updateProcess() {

        float energy = power * 10;
        int minY = WorldHeight.getMinHeight(worldObj, 1);
        int maxY = WorldHeight.getMaxHeight(worldObj, 255);

        for (long traceY = yCoord; traceY >= minY && energy > 0; traceY--) {
            int y = (int) traceY;
            List<Entity> entities = worldObj.getEntitiesWithinAABB(
                    Entity.class,
                    AxisAlignedBB.getBoundingBox(xCoord, y, zCoord, xCoord + 1, traceY + 1, zCoord + 1));
            for (Entity entity : entities) entity.attackEntityFrom(ChaosImplosion.chaosImplosion, power * 100);

            // energy -= block instanceof BlockLiquid ? 10 : block.getExplosionResistance(null);

            if (energy >= 0) worldObj.setBlockToAir(xCoord, y, zCoord);
            energy -= 0.5F + (0.1F * (yCoord - traceY));
        }

        energy = power * 20;
        long upperStart = (long) yCoord + 1;
        for (long traceY = upperStart; traceY < maxY && energy > 0; traceY++) {
            int y = (int) traceY;
            List<Entity> entities = worldObj.getEntitiesWithinAABB(
                    Entity.class,
                    AxisAlignedBB.getBoundingBox(xCoord, y, zCoord, xCoord + 1, traceY + 1, zCoord + 1));
            for (Entity entity : entities) entity.attackEntityFrom(ReactorExplosion.FUSION_EXPLOSION, power * 100);

            // energy -= block instanceof BlockLiquid ? 10 : block.getExplosionResistance(null);
            if (energy >= 0) worldObj.setBlockToAir(xCoord, y, zCoord);

            energy -= 0.5F + (0.1F * (traceY - upperStart));
        }

        isDead = true;
    }

    private boolean isDead = false;

    @Override
    public boolean isDead() {
        return isDead;
    }
}
