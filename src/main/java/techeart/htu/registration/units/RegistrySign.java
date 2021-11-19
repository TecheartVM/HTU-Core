package techeart.htu.registration.units;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.HTUCore;

import java.util.List;
import java.util.Map;

public class RegistrySign
{
    private final RegistryObject<Block> blockStanding;
    private final RegistryObject<Block> blockWall;
    private final RegistryObject<Item> item;

//    private final BlockEntityType<SignBlockEntity> blockEntityType;
    public RegistrySign(WoodType woodType, BlockBehaviour.Properties props, CreativeModeTab tab,
                        DeferredRegister<Block> br, DeferredRegister<Item> ir, DeferredRegister<BlockEntityType<?>> ber)
    {
        final Block s = new StandingSignBlock(props, woodType);
        Block w = new WallSignBlock(props, woodType);

        blockStanding = br.register("sign_" + woodType.name(), () -> s);
        blockWall = br.register("sign_wall_" + woodType.name(), () -> w);
        item = ir.register("sign_" + woodType.name(), () -> new SignItem(new Item.Properties().tab(tab), s, w));
    }

    public Block getBlockStanding() { return blockStanding.get(); }
    public Block getBlockWall() { return blockWall.get(); }
    public Item getItem() { return item.get(); }

    public static class SignRender implements BlockEntityRenderer<SignBlockEntity> {
        public static final int MAX_LINE_WIDTH = 90;
        private static final int LINE_HEIGHT = 10;
        private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
        private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
        private final Map<WoodType, SignRenderer.SignModel> signModels;
        private final Font font;

        public SignRender(BlockEntityRendererProvider.Context context) {
            this.signModels = HTUCore.BasicSignTypes.stream().collect(ImmutableMap.toImmutableMap((p_173645_) -> p_173645_, (p_173651_) -> new SignRenderer.SignModel(context.bakeLayer(ModelLayers.createSignModelName(p_173651_)))));
            this.font = context.getFont();
        }

        public void render(SignBlockEntity signBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
            BlockState blockstate = signBlockEntity.getBlockState();
            poseStack.pushPose();
            WoodType woodtype = getMaterial(blockstate.getBlock());
            SignRenderer.SignModel model = this.signModels.get(woodtype);
            if (blockstate.getBlock() instanceof StandingSignBlock) {
                poseStack.translate(0.5D, 0.5D, 0.5D);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-((float)(blockstate.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F)));
                model.stick.visible = true;
            } else {
                poseStack.translate(0.5D, 0.5D, 0.5D);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-blockstate.getValue(WallSignBlock.FACING).toYRot()));
                poseStack.translate(0.0D, -0.3125D, -0.4375D);
                model.stick.visible = false;
            }

            poseStack.pushPose();
            poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
            Material material = Sheets.getSignMaterial(woodtype);
            VertexConsumer vertexconsumer = material.buffer(bufferSource, model::renderType);
            model.root.render(poseStack, vertexconsumer, combinedLight, combinedOverlay);
            poseStack.popPose();
            poseStack.translate(0.0D, 0.33333334F, 0.046666667F);
            poseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
            int i = getDarkColor(signBlockEntity);
            FormattedCharSequence[] renderMessages = signBlockEntity.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (p_173653_) -> {
                List<FormattedCharSequence> list = this.font.split(p_173653_, MAX_LINE_WIDTH);
                return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
            });
            int k;
            boolean flag;
            int l;
            if (signBlockEntity.hasGlowingText()) {
                k = signBlockEntity.getColor().getTextColor();
                flag = isOutlineVisible(signBlockEntity, k);
                l = 15728880;
            } else {
                k = i;
                flag = false;
                l = combinedLight;
            }

            for(int i1 = 0; i1 < 4; ++i1) {
                FormattedCharSequence formattedcharsequence = renderMessages[i1];
                float f3 = (float)(-this.font.width(formattedcharsequence) / 2);
                if (flag) {
                    this.font.drawInBatch8xOutline(formattedcharsequence, f3, (float)(i1 * LINE_HEIGHT - 20), k, i, poseStack.last().pose(), bufferSource, l);
                } else {
                    this.font.drawInBatch(formattedcharsequence, f3, (float)(i1 * LINE_HEIGHT - 20), k, false, poseStack.last().pose(), bufferSource, false, 0, l);
                }
            }

            poseStack.popPose();
        }

        private boolean isOutlineVisible(SignBlockEntity p_173642_, int p_173643_) {
            if (p_173643_ == DyeColor.BLACK.getTextColor()) {
                return true;
            } else {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer localplayer = minecraft.player;
                if (localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.isScoping()) {
                    return true;
                } else {
                    Entity entity = minecraft.getCameraEntity();
                    return entity != null && entity.distanceToSqr(Vec3.atCenterOf(p_173642_.getBlockPos())) < (double)OUTLINE_RENDER_DISTANCE;
                }
            }
        }

        private int getDarkColor(SignBlockEntity p_173640_) {
            int i = p_173640_.getColor().getTextColor();
            int j = (int)((double) NativeImage.getR(i) * 0.4D);
            int k = (int)((double)NativeImage.getG(i) * 0.4D);
            int l = (int)((double)NativeImage.getB(i) * 0.4D);
            return i == DyeColor.BLACK.getTextColor() && p_173640_.hasGlowingText() ? BLACK_TEXT_OUTLINE_COLOR : NativeImage.combine(0, l, k, j);
        }

        public WoodType getMaterial(Block p_173638_) {
            WoodType woodtype;
            if (p_173638_ instanceof SignBlock) {
                woodtype = ((SignBlock)p_173638_).type();
            } else {
                woodtype = WoodType.OAK;
            }

            return woodtype;
        }
    }
}
