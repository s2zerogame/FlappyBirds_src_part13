/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;

/**
 *
 * @author phamn
 */
public class FlappyBirds extends GameScreen{

    private BufferedImage birds;
    private Animation bird_anim;
    
    
    public static float g = 0.1f;
    
    private Bird bird;
    private Ground ground;
    
    private ChimneyGroup chimneyGroup;
    
    private int Point = 0;
    
    private int BEGIN_SCREEN = 0;
    private int GAMEPLAY_SCREEN = 1;
    private int GAMEOVER_SCREEN = 2;
    
    private int CurrentScreen = BEGIN_SCREEN;
    
    public FlappyBirds(){
        super(800,600);
        
        try {
            birds = ImageIO.read(new File("Assets/bird_sprite.png"));
        } catch (IOException ex) {}
        
        bird_anim = new Animation(70);
        AFrameOnImage f;
        f = new AFrameOnImage(0,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(120,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60,0,60,60);
        bird_anim.AddFrame(f);
        
        bird = new Bird(350, 250, 50, 50);
        ground = new Ground();
        
        chimneyGroup = new ChimneyGroup();
        
        BeginGame();
    }
    
    
    public static void main(String[] args) {
        new FlappyBirds();
    }
    
    private void resetGame(){
        bird.setPos(350, 250);
        bird.setVt(0);
        bird.setLive(true);
        Point = 0;
        chimneyGroup.resetChimneys();
    }
    
    @Override
    public void GAME_UPDATE(long deltaTime) {
        
        if(CurrentScreen == BEGIN_SCREEN){
            resetGame();
        }else if(CurrentScreen == GAMEPLAY_SCREEN){
            
            if(bird.getLive()) bird_anim.Update_Me(deltaTime);
            bird.update(deltaTime);
            ground.Update();
            
            chimneyGroup.update();
            
            for(int i = 0;i<ChimneyGroup.SIZE;i++){
                if(bird.getRect().intersects(chimneyGroup.getChimney(i).getRect())){
                    bird.setLive(false);
                    System.out.println("Set live = false");
                }
                    
                
            }
            
            for(int i = 0;i<ChimneyGroup.SIZE;i++){
                if(bird.getPosX() > chimneyGroup.getChimney(i).getPosX() && !chimneyGroup.getChimney(i).getIsBehindBird()
                        && i%2==0){
                    Point ++;
                    chimneyGroup.getChimney(i).setIsBehindBird(true);
                }
                    
            }
            
            if(bird.getPosY() + bird.getH() > ground.getYGround()) CurrentScreen = GAMEOVER_SCREEN;
            
        }else{
            
        }
        
        
    }

    @Override
    public void GAME_PAINT(Graphics2D g2) {
        
        g2.setColor(Color.decode("#b8daef"));
        g2.fillRect(0, 0, MASTER_WIDTH, MASTER_HEIGHT);
        
        chimneyGroup.paint(g2);
        
        ground.Paint(g2);
        
        
        
        if(bird.getIsFlying())
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, -1);
        else 
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, 0);
        
        
        
        if(CurrentScreen == BEGIN_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to play game", 200, 300);
        }
        if(CurrentScreen == GAMEOVER_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to turn back begin screen", 200, 300);
        }
        
        g2.setColor(Color.red);
        g2.drawString("Point: "+Point, 20, 50);
    }

    @Override
    public void KEY_ACTION(KeyEvent e, int Event) {
        if(Event == KEY_PRESSED){
            
            if(CurrentScreen == BEGIN_SCREEN){
                
                CurrentScreen = GAMEPLAY_SCREEN;
                
            }else if(CurrentScreen == GAMEPLAY_SCREEN){
                if(bird.getLive()) bird.fly();
            }else if(CurrentScreen == GAMEOVER_SCREEN){
                CurrentScreen = BEGIN_SCREEN;
            }
        }
    }
    
}
