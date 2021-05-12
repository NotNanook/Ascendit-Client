package me.ascendit.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@Mixin({EntityArrow.class})
public class MixinEntityArrow extends Entity {

    public MixinEntityArrow(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public int ticksInGround;

    @Shadow
    public int xTile;

    @Shadow
    public int yTile;

    @Shadow
    public int zTile;

    @Shadow
    public Block inTile;

    @Shadow
    public int inData;

    @Shadow
    public boolean inGround;

    @Shadow
    public int canBePickedUp;

    @Shadow
    public int arrowShake;

    @Shadow
    public double damage;

    /**
     * @author Nanook
     * @reason Get rid of Arrow randomness
     */
    @Overwrite
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        motionX = x;
        motionY = y;
        motionZ = z;
        float f1 = MathHelper.sqrt_double(x * x + z * z);
        prevRotationYaw = rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float)(MathHelper.atan2(y, (double) f1) * 180.0D / Math.PI);
        ticksInGround = 0;
    }

    @Override
    public void entityInit() {
        dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        xTile = tagCompund.getShort("xTile");
        yTile = tagCompund.getShort("yTile");
        zTile = tagCompund.getShort("zTile");
        ticksInGround = tagCompund.getShort("life");

        if (tagCompund.hasKey("inTile", 8)) {
            inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        } else {
            inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        inData = tagCompund.getByte("inData") & 255;
        arrowShake = tagCompund.getByte("shake") & 255;
        inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("damage", 99)) {
            damage = tagCompund.getDouble("damage");
        }

        if (tagCompund.hasKey("pickup", 99)) {
            canBePickedUp = tagCompund.getByte("pickup");
        } else if (tagCompund.hasKey("player", 99)) {
            canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short) xTile);
        tagCompound.setShort("yTile", (short) yTile);
        tagCompound.setShort("zTile", (short) zTile);
        tagCompound.setShort("life", (short) ticksInGround);
        ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte) inData);
        tagCompound.setByte("shake", (byte) arrowShake);
        tagCompound.setByte("inGround", (byte)(inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte) canBePickedUp);
        tagCompound.setDouble("damage", damage);
    }
}