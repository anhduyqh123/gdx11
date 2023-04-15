package com.game;

import GDX11.GDXGame;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    private Texture mask;
    private Texture sprite;
    private SpriteBatch batch,spriteBatch;
    private FrameBuffer buffer;
    @Override
    public void create() {
        batch = new SpriteBatch();
        spriteBatch = new SpriteBatch();
        mask = new Texture("block_1.png");
        sprite = new Texture("sprite.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        spriteBatch.enableBlending();
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glClearStencil(0);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 1);

        spriteBatch.begin();
        spriteBatch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
        spriteBatch.draw(mask, 0, 0);
        spriteBatch.flush();

        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 1);
        //draw child
        spriteBatch.draw(sprite, 0, 0);
        spriteBatch.end();

        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);


    }
}
