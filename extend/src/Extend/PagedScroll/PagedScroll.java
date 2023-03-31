package Extend.PagedScroll;

import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedScroll extends ScrollPane {
    private boolean wasPanDragFling = false;

    public boolean scaleChildDist = false;
    public float spaceX,spaceY;
    public Table content;
    public Actor current;
    private GDX.Runnable1<Actor> onScroll;

    public PagedScroll () {
        super(null);
        Setup();
    }
    private void Setup() {
        content = new Table();
        super.setWidget(content);
    }

    public void AddScrollEvent(GDX.Runnable1<Actor> onScroll)
    {
        this.onScroll = onScroll;
    }
    private void OnScroll(Actor actor)
    {
        if (onScroll!=null) onScroll.Run(actor);
    }
    private void ScrollByStep(int del)//
    {
        if (current==null) return;
        int index = content.getChildren().indexOf(current,false)+del;
        int size = content.getChildren().size;
        if (index>=size) index=0;
        if (index<0) index = size-1;
        ScrollTo(content.getChild(index));
    }
    public void Next()
    {
        ScrollByStep(1);
    }
    public void Previous()
    {
        ScrollByStep(-1);
    }

    @Override
    public void addActor(Actor actor) {
        content.add(actor).spaceRight(spaceX).spaceTop(spaceY);
    }

    @Override
    public void act (float delta) {
        if (scaleChildDist) ScaleChild();
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
            ScrollToPage();
        } else {
            if (isPanning() || isDragging() || isFlinging()) {
                wasPanDragFling = true;
            }
        }
    }

    private void ScrollToPage() {
        final float width = getWidth();
        final float scrollX = getScrollX() + getWidth() / 2f;

        Array<Actor> pages = content.getChildren();
        if (pages.size > 0) {
            for (Actor a : pages) {
                float pageX = a.getX();
                float pageWidth = a.getWidth();

                if (scrollX >= pageX - pageWidth && scrollX < pageX + pageWidth)
                {
                    this.current = a;
                    setScrollX(pageX - (width - pageWidth) / 2f);
                    OnScroll(a);
                    break;
                }
            }
        }
    }

    public void ScrollTo(Actor actor)
    {
        this.current = actor;
        float pageX = actor.getX();
        float pageWidth = actor.getWidth();
        setScrollX(pageX - (getWidth() - pageWidth) / 2f);
    }

    @Override
    public void clear() {
        clearActions();
        content.clearChildren();
    }

    @Override
    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
        content.setDebug(enabled);
    }
    private void ScaleChild() {
        float midpoint = getWidth() / 2.f;
        float d0 = 0.f;
        float mShrinkDistance = 0.9f;
        float d1 = mShrinkDistance * midpoint;
        float s0 = 1.f;
        float mShrinkAmount = 0.4f;
        float s1 = 1.f - mShrinkAmount;
        for (int i = 0; i < content.getChildren().size; i++) {
            Actor child = content.getChild(i);
            float childMidpoint = (child.getX()+content.getX()+(child.getWidth()/2));
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}
