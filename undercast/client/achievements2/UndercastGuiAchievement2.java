package undercast.client.achievements2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class UndercastGuiAchievement2 extends GuiScreen {

    private Minecraft client;
    private ArrayList<UndercastAchievement> achievements = new ArrayList();
    private static final ResourceLocation achievementBackground = new ResourceLocation(
            "textures/gui/achievement/achievement_background.png");

    public UndercastGuiAchievement2(Minecraft mc) {
        client = mc;
    }

    public void queueTakenAchievement(UndercastAchievement achievement) {
        achievements.add(achievement);
    }

    public void updateScreenXTimes(int x) {
        for (int i = 0; i < x; i++) {
            updateScreen();
        }
    }

    @Override
    public void updateScreen() {
        ScaledResolution scr = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

        for (int achievementIndex = 0; achievementIndex < achievements.size(); achievementIndex++) {
            GL11.glPushMatrix();
            final UndercastAchievement ac = achievements.get(achievementIndex);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            float y = 0;
            if (ac.achievementTime < 256) {
                y = -28 + (ac.achievementTime/8) + (ac.achievementTime % 24) / 24;
            } else {
                y = 4;
            }

            GL11.glTranslatef(scr.getScaledWidth() - 168 + ac.xOffset, y + 36 * achievementIndex, 0.0F);
            client.func_110434_K().func_110577_a(achievementBackground);
            this.drawTexturedModalRect(4, 4, 96, 202, 160, 32);
            this.drawString(client.fontRenderer, ac.line1, 40, 10, 16777215);
            this.drawString(client.fontRenderer, ac.line2, 40, 22, 16777215);
            String str = ac.killerName;
            ResourceLocation resourcelocation = AbstractClientPlayer.func_110311_f(str);
            AbstractClientPlayer.func_110304_a(resourcelocation, str);
            GL11.glPushMatrix(); // New GL11 matrix to not affect other part of the gui
            TextureManager texturemanager = Minecraft.getMinecraft().renderEngine;
            if (texturemanager != null) {
                texturemanager.func_110577_a(resourcelocation);
            }
            GL11.glScalef(1F / 2F, 1F / 4F, 1F);// Resizing the image (height/4 and width/8)
            this.drawTexturedModalRect(24, 48, 32, 64, 32, 64); // Drawing the image
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            ac.achievementTime++;
            if(ac.achievementTime > 5120){
                if(ac.achievementTime % 5 == 0){
                    ac.xOffset+= ac.xOffset / 20;
                }
                if(ac.xOffset > 170){
                    achievements.remove(achievementIndex);
                }
            }
        }
    }


}
