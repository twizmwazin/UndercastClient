package undercast.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import undercast.client.UndercastConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAchievementDurationSlider extends GuiButton {
    /** The value of this slider control. */
    public float sliderValue = 1.0F;

    /** Is this slider control being dragged. */
    public boolean dragging;

    public GuiAchievementDurationSlider(int par1, int par2, int par3, String par5Str, float par6) {
        super(par1, par2, par3, 150, 20, par5Str);
        this.sliderValue = par6 / 4F;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over this button.
     */
    protected int getHoverState(boolean par1) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.field_146125_m) {
            if (this.dragging) {
                this.sliderValue = (float) (par2 - (this.field_146128_h + 4)) / (float) (this.field_146120_f - 8);

                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }
                float displayedNumber = (((float) (int) (sliderValue * 40)) / 10F);
                this.field_146126_j = "Duration: " + ((displayedNumber > 0.0F) ? displayedNumber + "sec" : "OFF");
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.field_146128_h + (int) (this.sliderValue * (float) (this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.field_146128_h + (int) (this.sliderValue * (float) (this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
     */
    public boolean func_146116_c(Minecraft par1Minecraft, int par2, int par3) {
        if (super.func_146116_c(par1Minecraft, par2, par3)) {
            this.sliderValue = (float) (par2 - (this.field_146128_h + 4)) / (float) (this.field_146120_f - 8);

            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }
            if (sliderValue > 0.0F && sliderValue < 1.0F) {
                this.field_146126_j = "Duration: " + ((float) (int) (sliderValue * 40)) / 10F + "sec";
            } else {
                sliderValue = 0.0F;
                this.field_146126_j = "Duration: OFF";
            }
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
        UndercastConfig.setDoubleProperty("achievementAnimationDuration", ((double) (int) (sliderValue * 40)) / 10D);
        UndercastConfig.reloadConfig();
    }
}
