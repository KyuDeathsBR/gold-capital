package com.kyuuzinbr.gldcmod.blocks;

import com.kyuuzinbr.gldcmod.GLDCModRegistries;
import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GodlySkullBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    @Nullable
    private BlockPattern kratosBase;
    @Nullable
    private BlockPattern kratosFull;



    public GodlySkullBlock() {
        super(Properties.of(Material.DECORATION));
    }
    protected static final VoxelShape AABB = Block.box(3.0D, 0.0D, 2.0D, 13.0D, 13.0D, 12.0D);
    protected static final VoxelShape AABBInverted = Block.box(2.0D, 0.0D, 3.0D, 12.0D, 13.0D, 13.0D);

    public GodlySkullBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return switch (blockState.getValue(FACING)) {
            case DOWN, UP -> null;
            case NORTH, SOUTH -> AABB;
            case WEST, EAST -> AABBInverted;
        };
    }

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (!blockState1.is(blockState.getBlock())) {
            this.trySpawnKratos(level, blockPos);
        }
    }

    public boolean canSpawnKratos(LevelReader levelReader, BlockPos blockPos) {
        return this.getOrCreateKratosBase().find(levelReader, blockPos) != null;
    }

    private void trySpawnKratos(Level level, BlockPos blockPos) {
        BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = this.getOrCreateKratosFull().find(level, blockPos);
        blockpattern$blockpatternmatch = this.getOrCreateKratosFull().find(level, blockPos);
        if (blockpattern$blockpatternmatch != null) {
            for(int j = 0; j < this.getOrCreateKratosFull().getWidth(); ++j) {
                for(int k = 0; k < this.getOrCreateKratosFull().getHeight(); ++k) {
                    BlockInWorld blockinworld2 = blockpattern$blockpatternmatch.getBlock(j, k, 0);
                    level.setBlock(blockinworld2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld2.getPos(), Block.getId(blockinworld2.getState()));
                }
            }

            BlockPos blockpos = blockpattern$blockpatternmatch.getBlock(1, 2, 0).getPos();
            KratosMessi kratos = GLDCModRegistries.GLDCModEntityRegistry.KRATOS_MESSI.get().create(level);
            kratos.moveTo((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(kratos);

            for(ServerPlayer serverplayer1 : level.getEntitiesOfClass(ServerPlayer.class, kratos.getBoundingBox().inflate(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer1, kratos);
            }

            for(int i1 = 0; i1 < this.getOrCreateKratosFull().getWidth(); ++i1) {
                for(int j1 = 0; j1 < this.getOrCreateKratosFull().getHeight(); ++j1) {
                    BlockInWorld blockinworld1 = blockpattern$blockpatternmatch.getBlock(i1, j1, 0);
                    level.blockUpdated(blockinworld1.getPos(), Blocks.AIR);
                }
            }
        }

    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private BlockPattern getOrCreateKratosBase() {
        if (this.kratosBase == null) {
            this.kratosBase = BlockPatternBuilder.start().aisle("~ ~", "#-#", "~0~").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.BLUE_WOOL))).where('0', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COAL_BLOCK))).where('-',BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.REDSTONE_BLOCK))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }

        return this.kratosBase;
    }

    private BlockPattern getOrCreateKratosFull() {
        if (this.kratosFull == null) {
            this.kratosFull = BlockPatternBuilder.start().aisle("~^~", "#-#", "~0~").where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(GLDCModRegistries.GLDCModBlockRegistry.GODLY_SKULL_BLOCK.get()))).where('0', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COAL_BLOCK))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.BLUE_WOOL))).where('-',BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.REDSTONE_BLOCK))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }

        return this.kratosFull;
    }
}
