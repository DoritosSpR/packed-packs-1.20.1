package io.github.fishstiz.fidgetz.gui.layouts;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class FlexLayout implements Layout {
    private final LinearLayout wrappedLayout;
    private int spacing;

    private FlexLayout(LinearLayout.Orientation orientation) {
        this.wrappedLayout = new LinearLayout(0, 0, orientation);
    }

    public static FlexLayout horizontal(IntSupplier maxWidth) {
        return new FlexLayout(LinearLayout.Orientation.HORIZONTAL);
    }

    public static FlexLayout vertical(IntSupplier maxHeight) {
        return new FlexLayout(LinearLayout.Orientation.VERTICAL);
    }

    public FlexLayout spacing(int spacing) {
        this.spacing = spacing;
        this.wrappedLayout.spacing(spacing);
        return this;
    }

    public <T extends AbstractWidget> T addFlexChild(T child) {
        return this.wrappedLayout.addChild(child);
    }

    public <T extends AbstractWidget> T addFlexChild(T child, LayoutSettings settings) {
        return this.wrappedLayout.addChild(child, settings);
    }

    @Override public void arrangeElements() { this.wrappedLayout.arrangeElements(); }
    @Override public int getWidth() { return this.wrappedLayout.getWidth(); }
    @Override public int getHeight() { return this.wrappedLayout.getHeight(); }
    @Override public void setX(int x) { this.wrappedLayout.setX(x); }
    @Override public void setY(int y) { this.wrappedLayout.setY(y); }
    @Override public int getX() { return this.wrappedLayout.getX(); }
    @Override public int getY() { return this.wrappedLayout.getY(); }
    @Override public void visitChildren(Consumer<LayoutElement> visitor) { this.wrappedLayout.visitChildren(visitor); }
}
