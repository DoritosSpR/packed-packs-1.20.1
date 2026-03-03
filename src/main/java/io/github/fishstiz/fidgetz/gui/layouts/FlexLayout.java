package io.github.fishstiz.fidgetz.gui.layouts;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class FlexLayout implements Layout {
    private final List<Child<? extends LayoutElement>> children = new ObjectArrayList<>();
    private final LinearLayout wrappedLayout;
    private final LinearLayout.Orientation orientation;
    private final @Nullable IntSupplier maxSizeAtOrientation;
    private int minWidth;
    private int minHeight;
    private int spacing;

    private FlexLayout(LinearLayout.Orientation orientation, @Nullable IntSupplier maxSizeAtOrientation, int minWidth, int minHeight, int spacing) {
        this.orientation = orientation;
        // Constructor 1.20.1: width, height, orientation
        this.wrappedLayout = new LinearLayout(0, 0, orientation);
        this.maxSizeAtOrientation = maxSizeAtOrientation;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.spacing = spacing;
    }

    public <T extends LayoutElement> T addChild(T child, LayoutSettings layoutSettings) {
        this.children.add(new Child<>(child, layoutSettings));
        return this.wrappedLayout.addChild(child, layoutSettings);
    }

    public <T extends LayoutElement> T addChild(T child) {
        return this.wrappedLayout.addChild(child);
    }

    public <T extends AbstractWidget> T addFlexChild(T child, boolean crossAxis, LayoutSettings layoutSettings) {
        this.children.add(new FlexWidget(child, crossAxis, layoutSettings));
        return this.wrappedLayout.addChild(child, layoutSettings);
    }

    @Override
    public void arrangeElements() {
        int distribution = this.getFlexDistribution();
        for (Child<?> child : this.children) {
            if (child instanceof FlexChild<?> flexChild) {
                flexChild.setDistribution(this.orientation, distribution, this.minWidth, this.minHeight);
            }
        }
        this.wrappedLayout.arrangeElements();
    }

    private int getFlexDistribution() {
        int totalSize = 0;
        int flexCount = 0;
        for (int i = 0; i < this.children.size(); i++) {
            Child<?> child = this.children.get(i);
            if (child instanceof FlexChild<?>) flexCount++;
            else totalSize += child.getSizeAtOrientation(this.orientation);
            if (i < this.children.size() - 1) totalSize += spacing;
        }
        int max = this.maxSizeAtOrientation != null ? this.maxSizeAtOrientation.getAsInt() : (this.orientation == LinearLayout.Orientation.HORIZONTAL ? minWidth : minHeight);
        return flexCount > 0 ? Math.max(0, (max - totalSize) / flexCount) : 0;
    }

    @Override public void visitChildren(Consumer<LayoutElement> visitor) { this.wrappedLayout.visitChildren(visitor); }
    @Override public void setX(int x) { this.wrappedLayout.setX(x); }
    @Override public void setY(int y) { this.wrappedLayout.setY(y); }
    @Override public int getX() { return this.wrappedLayout.getX(); }
    @Override public int getY() { return this.wrappedLayout.getY(); }
    @Override public int getWidth() { return this.wrappedLayout.getWidth(); }
    @Override public int getHeight() { return this.wrappedLayout.getHeight(); }

    public static FlexLayout horizontal(IntSupplier maxWidth) { return new FlexLayout(LinearLayout.Orientation.HORIZONTAL, maxWidth, 0, 0, 0); }
    public static FlexLayout vertical(IntSupplier maxHeight) { return new FlexLayout(LinearLayout.Orientation.VERTICAL, maxHeight, 0, 0, 0); }

    private static class Child<T extends LayoutElement> {
        protected final T element;
        protected final LayoutSettings layoutSettings;
        protected Child(T element, LayoutSettings layoutSettings) { this.element = element; this.layoutSettings = layoutSettings; }
        protected int getSizeAtOrientation(LinearLayout.Orientation orientation) { return orientation == LinearLayout.Orientation.HORIZONTAL ? element.getWidth() : element.getHeight(); }
    }

    private abstract static class FlexChild<T extends LayoutElement> extends Child<T> {
        protected final boolean crossAxis;
        protected FlexChild(T element, boolean crossAxis, LayoutSettings layoutSettings) { super(element, layoutSettings); this.crossAxis = crossAxis; }
        protected abstract void setWidth(int width);
        protected abstract void setHeight(int height);
        protected void setDistribution(LinearLayout.Orientation orientation, int distribution, int width, int height) {
            if (orientation == LinearLayout.Orientation.HORIZONTAL) {
                this.setWidth(distribution);
                if (this.crossAxis && height > 0) this.setHeight(height);
            } else {
                this.setHeight(distribution);
                if (this.crossAxis && width > 0) this.setWidth(width);
            }
        }
    }

    private static class FlexWidget extends FlexChild<AbstractWidget> {
        private FlexWidget(AbstractWidget element, boolean crossAxis, LayoutSettings layoutSettings) { super(element, crossAxis, layoutSettings); }
        @Override protected void setWidth(int width) { this.element.setWidth(width); }
        @Override protected void setHeight(int height) { this.element.setHeight(height); }
    }
}
