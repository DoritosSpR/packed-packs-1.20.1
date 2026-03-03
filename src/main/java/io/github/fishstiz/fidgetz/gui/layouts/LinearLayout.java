package io.github.fishstiz.fidgetz.gui.layouts;

import net.minecraft.client.gui.layouts.AbstractLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LinearLayout extends AbstractLayout {
    private final List<LayoutElement> children = new ArrayList<>();
    private int spacing = 0;

    public LinearLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public static LinearLayout vertical() {
        return new LinearLayout(0, 0, 0, 0);
    }

    public LinearLayout spacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public <T extends LayoutElement> T addChild(T child) {
        this.children.add(child);
        return child;
    }

    @Override
    public void setX(int x) { super.setX(x); arrangeElements(); }
    @Override
    public void setY(int y) { super.setY(y); arrangeElements(); }

    @Override
    public void arrangeElements() {
        int currentY = this.getY();
        for (LayoutElement child : children) {
            child.setX(this.getX());
            child.setY(currentY);
            currentY += child.getHeight() + spacing;
        }
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
        children.forEach(consumer);
    }
}
