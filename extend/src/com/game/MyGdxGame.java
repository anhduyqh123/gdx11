package com.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


public class MyGdxGame implements ApplicationListener {

    OrthographicCamera cam;
    SpriteBatch batch;
    Texture bg, sprite, alphaMask;

    @Override
    public void create () {
        cam = new OrthographicCamera();
        batch = new SpriteBatch();

        sprite = new Texture("btn_grn.png");
        alphaMask = new Texture("block_1.png");
    }

    @Override
    public void resize (int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);
    }

    private void drawBackground(SpriteBatch batch) {
        //regular blending mode
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //... draw background entities/tiles here ...


        //flush the batch to the GPU
        batch.flush();
    }

    private void drawAlphaMask(SpriteBatch batch, float x, float y) {
        //disable RGB color, only enable ALPHA to the frame buffer
        Gdx.gl.glColorMask(false, false, false, true);

        //change the blending function for our alpha map
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);

        //draw alpha mask sprite(s)
        batch.draw(alphaMask, x, y, alphaMask.getWidth(), alphaMask.getHeight());

        //flush the batch to the GPU
        batch.flush();
    }

    private void drawForeground(SpriteBatch batch) {
        //now that the buffer has our alpha, we simply draw the sprite with the mask applied
        Gdx.gl.glColorMask(true, true, true, true);
        batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

        //The scissor test is optional, but it depends
//        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        //Gdx.gl.glScissor(clipX, clipY, clipWidth, clipHeight);

        //draw our sprite to be masked
        batch.draw(sprite, 0, 0, sprite.getWidth(), sprite.getHeight());

        //remember to flush before changing GL states again
        batch.flush();

        //disable scissor before continuing
//        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    @Override
    public void render () {
        ScreenUtils.clear(Color.ROYAL);
        //Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        //start the batch
        batch.begin();

        //draw background
        drawBackground(batch);


        //the sprite we want the circle mask applied to

        //draw the alpha mask
        drawAlphaMask(batch, 20, 30);

        //draw our foreground elements
        drawForeground(batch);

        batch.end();
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
        batch.dispose();
        alphaMask.dispose();
        sprite.dispose();
    }


}
