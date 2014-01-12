package undercast.client;

import org.lwjgl.opengl.GL11;

import undercast.network.common.packet.VIPUser;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

public class UndercastRenderHandler {
	public UndercastRenderHandler(){
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void renderEvent(RenderPlayerEvent.Specials.Pre evt){
		VIPUser player = null;
		for(VIPUser u : UndercastModClass.getInstance().vips){
			if (u.getUsername().equals(evt.entityPlayer.getDisplayName())){
				player = u;
				if(!player.hasCape()){
					return;
				}
			}
		}
        if (player != null && !evt.entityPlayer.isInvisible() && !evt.entityPlayer.getHideCape())
        {
        	if(player.getCape() == VIPUser.DEVELOPER_CAPE){
        		/*String fileName = "developer_";
        		if((25 - UndercastModClass.capeCounter - 1) > 9){
        			fileName+="00" + (25 - UndercastModClass.capeCounter - 1);
        		} else{
        			fileName+="000" + (25 - UndercastModClass.capeCounter - 1);
        		}
        		fileName += "_Layer-" + (UndercastModClass.capeCounter + 1) + ".png";*/
        		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("undercast","temp.png"));
        	}else{
        		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("undercast","minecon.png"));
        	}
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = evt.entityPlayer.field_71091_bM + (evt.entityPlayer.field_71094_bP - evt.entityPlayer.field_71091_bM) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosX + (evt.entityPlayer.posX - evt.entityPlayer.prevPosX) * (double)evt.partialRenderTick);
            double d4 = evt.entityPlayer.field_71096_bN + (evt.entityPlayer.field_71095_bQ - evt.entityPlayer.field_71096_bN) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosY + (evt.entityPlayer.posY - evt.entityPlayer.prevPosY) * (double)evt.partialRenderTick);
            double d0 = evt.entityPlayer.field_71097_bO + (evt.entityPlayer.field_71085_bR - evt.entityPlayer.field_71097_bO) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosZ + (evt.entityPlayer.posZ - evt.entityPlayer.prevPosZ) * (double)evt.partialRenderTick);
            float f5 = evt.entityPlayer.prevRenderYawOffset + (evt.entityPlayer.renderYawOffset - evt.entityPlayer.prevRenderYawOffset) * evt.partialRenderTick;
            double d1 = (double)MathHelper.sin(f5 * (float)Math.PI / 180.0F);
            double d2 = (double)(-MathHelper.cos(f5 * (float)Math.PI / 180.0F));
            float f6 = (float)d4 * 10.0F;

            if (f6 < -6.0F)
            {
                f6 = -6.0F;
            }

            if (f6 > 32.0F)
            {
                f6 = 32.0F;
            }

            float f7 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f8 = (float)(d3 * d2 - d0 * d1) * 100.0F;

            if (f7 < 0.0F)
            {
                f7 = 0.0F;
            }

            float f9 = evt.entityPlayer.prevCameraYaw + (evt.entityPlayer.cameraYaw - evt.entityPlayer.prevCameraYaw) * evt.partialRenderTick;
            f6 += MathHelper.sin((evt.entityPlayer.prevDistanceWalkedModified + (evt.entityPlayer.distanceWalkedModified - evt.entityPlayer.prevDistanceWalkedModified) * evt.partialRenderTick) * 6.0F) * 32.0F * f9;

            if (evt.entityPlayer.isSneaking())
            {
                f6 += 25.0F;
            }

            GL11.glRotatef(6.0F + f7 / 2.0F + f6, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f8 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f8 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            ModelBiped modelBipedMain = ReflectionHelper.getPrivateValue(RenderPlayer.class, evt.renderer, 1);
            modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
	}
}
