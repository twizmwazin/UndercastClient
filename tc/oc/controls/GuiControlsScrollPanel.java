package tc.oc.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiControlsScrollPanel extends GuiSlot {
    private GuiAresControls controls;
    private GameSettings options;
    private Minecraft mc;
    private int _mouseX;
    private int _mouseY;
    private int selected = -1;

    public GuiControlsScrollPanel(GuiAresControls controls, GameSettings options, Minecraft mc) {
        super(mc, controls.width, controls.height, 16, (controls.height - 32) + 4, 25);
        this.controls = controls;
        this.options = options;
        this.mc = mc;
    }

    @Override
    protected int getSize() {
        return this.options.keyBindings.length;
    }

    @Override
    protected void elementClicked(int i, boolean flag) {
        if(!flag) {
            if(this.selected == -1) {
                this.selected = i;
            } else {
                this.options.setKeyBinding(this.selected, -100);
                this.selected = -1;
                KeyBinding.resetKeyBindingArrayAndHash();
            }
        }
    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    public void drawScreen(int mX, int mY, float f) {
        this._mouseX = mX;
        this._mouseY = mY;

        if(this.selected != -1 && !Mouse.isButtonDown(0) && Mouse.getDWheel() == 0) {
            if(Mouse.next() && Mouse.getEventButtonState()) {
                this.options.setKeyBinding(this.selected, -100 + Mouse.getEventButton());
                this.selected = -1;
                KeyBinding.resetKeyBindingArrayAndHash();
            }
        }

        super.drawScreen(mX, mY, f);
    }

    @Override
    protected void drawSlot(int index, int xPosition, int yPosition, int l, Tessellator tessellator) {
        int width = 70;
        int height = 20;
        xPosition -= 20;
        boolean flag = this._mouseX >= xPosition && this._mouseY >= yPosition && this._mouseX < xPosition + width && this._mouseY < yPosition + height;
        int k = (flag ? 2 : 1);
        this.mc.renderEngine.bindTexture("/gui/gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.controls.drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
        this.controls.drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);
        this.controls.drawString(this.mc.fontRenderer, this.options.getKeyBindingDescription(index), xPosition + width + 4, yPosition + 6, 0xFFFFFFFF);
        boolean conflict = false;

        for(int x = 0; x < this.options.keyBindings.length; x++) {
            if(x != index && this.options.keyBindings[x].keyCode == this.options.keyBindings[index].keyCode) {
                conflict = true;
                break;
            }
        }

        String str = (conflict ? EnumChatFormatting.RED : "") + this.options.getOptionDisplayString(index);
        str = (index == this.selected ? EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + "??? " + EnumChatFormatting.WHITE + "<" : str);
        this.controls.drawCenteredString(this.mc.fontRenderer, str, xPosition + (width / 2), yPosition + (height - 8) / 2, 0xFFFFFFFF);
    }

    public boolean keyTyped(char c, int i) {
        if(this.selected != -1) {
            this.options.setKeyBinding(this.selected, i);
            this.selected = -1;
            KeyBinding.resetKeyBindingArrayAndHash();
            return false;
        }

        return true;
    }
}
