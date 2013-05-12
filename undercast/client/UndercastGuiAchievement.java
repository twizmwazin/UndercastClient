package undercast.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class UndercastGuiAchievement extends GuiAchievement {

    /**
     * Holds the instance of the game (Minecraft)
     */
    private Minecraft theGame;
    /**
     * Holds the latest width scaled to fit the game window.
     */
    private int achievementWindowWidth;
    /**
     * Holds the latest height scaled to fit the game window.
     */
    private int achievementWindowHeight;
    private String achievementGetLocalText;
    private String achievementStatName;
    /**
     * Holds the achievement that will be displayed on the GUI.
     */
    private Achievement theAchievement;
    private long achievementTime;
    /**
     * Holds a instance of RenderItem, used to draw the achievement icons on
     * screen (is based on ItemStack)
     */
    private RenderItem itemRender;
    private boolean haveAchiement;
    public boolean isFakeAchievement;
    public String kindOfKill;
    //True for killed, false for died
    public boolean killedOrDied;
    BufferedReader buffer = null;
    FileOutputStream fos = null;
    InputStream is = null;
    private BufferedImage buffered;
    private ImageLoader imgLoader;
    private String killerName;
    private String firstLine;
    private String secondLine;

    public UndercastGuiAchievement(Minecraft par1Minecraft) {
        super(par1Minecraft);
        this.theGame = par1Minecraft;
        this.itemRender = new RenderItem();
        isFakeAchievement = false;
        killedOrDied = false;
        this.imgLoader = new ImageLoader(par1Minecraft.texturePackList, par1Minecraft.gameSettings);
    }

    /**
     * Queue a taken achievement to be displayed.
     */
    public void queueTakenAchievement(Achievement par1Achievement) {
        this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
        this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
        this.isFakeAchievement = false;


    }

    public void addFakeAchievementToMyList(Achievement par1Achievement, boolean killedOrDied, String killerName) {
        this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
        this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
        this.isFakeAchievement = true;
        this.killedOrDied = killedOrDied;
        this.killerName = killerName;
        this.firstLine = this.killerName;
        this.secondLine = this.killedOrDied ? "+1 Kill" : "+1 Death";
    }

    public void addFakeAchievementToMyList(Achievement par1Achievement, boolean killedOrDied, String killerName, String firstLine, String secondLine) {
        this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
        this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
        this.isFakeAchievement = true;
        this.killedOrDied = killedOrDied;
        this.killerName = killerName;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    /**
     * Queue a information about a achievement to be displayed.
     */
    public void queueAchievementInformation(Achievement par1Achievement) {
        this.achievementGetLocalText = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementStatName = par1Achievement.getDescription();
        this.achievementTime = Minecraft.getSystemTime() - 2500L;
        this.theAchievement = par1Achievement;
        this.haveAchiement = true;
        this.isFakeAchievement = false;

    }

    /**
     * Update the display of the achievement window to match the game window.
     */
    private void updateAchievementWindowScale() {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.achievementWindowWidth = this.theGame.displayWidth;
        this.achievementWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.achievementWindowWidth = scaledresolution.getScaledWidth();
        this.achievementWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double) this.achievementWindowWidth, (double) this.achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    /**
     * Updates the small achievement tooltip window, showing a queued
     * achievement if is needed.
     */
    public void updateAchievementWindow() {
        if (this.theAchievement != null && this.achievementTime != 0L) {
            double d0 = (double) (Minecraft.getSystemTime() - this.achievementTime) / 3000.0D;

            if (!this.haveAchiement && (d0 < 0.0D || d0 > 1.0D)) {
                this.achievementTime = 0L;
            } else {
                this.updateAchievementWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D) {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D) {
                    d1 = 0.0D;
                }

                d1 *= d1;
                d1 *= d1;
                int i = this.achievementWindowWidth - 160;
                int j = 0 - (int) (d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.theGame.renderEngine.bindTexture("/achievement/bg.png");
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

                if (this.haveAchiement) {
                    this.theGame.fontRenderer.drawSplitString(this.achievementStatName, i + 30, j + 7, 120, -1);
                } else if (this.isFakeAchievement) {
                    this.theGame.fontRenderer.drawString(this.firstLine, i + 30, j + 7, this.killedOrDied ? 52224 : 13369344);
                    this.theGame.fontRenderer.drawString(this.secondLine, i + 30, j + 18, this.killedOrDied ? 52224 : 13369344);
                } else {
                    this.theGame.fontRenderer.drawString(this.achievementGetLocalText, i + 30, j + 7, -256);
                    this.theGame.fontRenderer.drawString(this.achievementStatName, i + 30, j + 18, -1);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                if (!this.isFakeAchievement) {
                    this.itemRender.renderItemAndEffectIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, this.theAchievement.theItemStack, i + 8, j + 8);
                } else {
                    GL11.glPushMatrix(); // New GL11 matrix to not affect other part of the gui
                    try {
                        //Loading the buffer as a readable image and set it as GL11 texture
                        //100 is a unique id, and both 16 are for the size of the image
                        this.imgLoader.setupTexture(UndercastKillsHandler.killerBuffer, 100, 16, 16);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    GL11.glColor4f(1, 1, 1, 1); // White light on the image
                    GL11.glScalef(1F / 16F, 1F / 16F, 1F);// Resizing the image (divided by 16 in the diagonal)
                    GL11.glTranslatef((i + 8) * 16F, (j + 8) * 16F, 0);// Translating the image in the gui
                    this.drawTexturedModalRect(0, 0, 0, 0, 256, 256); // Drawing the image
                    GL11.glPopMatrix();
                }
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
