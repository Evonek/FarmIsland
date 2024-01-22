import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.util.NoSuchElementException;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.awt.Desktop;
/**
 * Changes:
 * StartWorld
 */
public class StartWorld extends World
{
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private GreenfootImage background = new GreenfootImage("BackGrounds/Start BG Back.png");
    private GreenfootImage clouds = new GreenfootImage("BackGrounds/Start BG Clouds.png");
    private GreenfootImage island = new GreenfootImage("BackGrounds/Start BG Island.png");

    private GreenfootImage screen = new GreenfootImage(430, 720);

    private HighlightButton startButton;
    private HighlightButton load;
    private HighlightButton settings;
    private HighlightButton quit;

    private Scanner scan;
    private Scanner fileScanner;
    private String fileName;
    private static ArrayList<String> saveFile;

    private GreenfootSound TitleScreenMusic;
    public StartWorld()
    {   
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        //screen.setColor(new Color(255,255,255,45));
        //screen.fillPolygon(new int[] {0,0,430,390}, new int[] {0,720,720,0}, 4);

        //background.drawImage(screen, 0, 0);
        setBackground(background);

        initialize();
    }

    public void act(){
        //button actions here
        if(startButton.leftClickedThis()){
            TitleScreenMusic.stop();
            Greenfoot.setWorld(new GameWorld(null));
        }
        if(load.leftClickedThis()){
            //Open();

            scan = new Scanner(System.in);
            System.out.println("Enter Your File Name: ");
            fileName = scan.nextLine();
            scan.close();
            try{
                fileScanner = new Scanner (new File(fileName));
                Greenfoot.setWorld(new GameWorld(fileName));
            }
            catch(FileNotFoundException e){
                System.out.println("Invalid File");
            }
            fileScanner.close();
        }
        if(settings.leftClickedThis()){
            Greenfoot.setWorld(new SettingsWorld());
            TitleScreenMusic.stop();
        }
        if(quit.leftClickedThis()){
            Greenfoot.stop();
        }
    }

    public void initialize(){
        TitleScreenMusic = new GreenfootSound ("TItleScreenMusic.mp3");
        TitleScreenMusic.setVolume(50);
        setPaintOrder(Button.class, Effect.class);
        startButton = new HighlightButton("Start Menu");
        load = new HighlightButton("Start Menu");
        settings = new HighlightButton("Start Menu");
        quit = new HighlightButton("Start Menu");
        
        startButton.setTransparancy(230);
        load.setTransparancy(230);
        settings.setTransparancy(230);
        quit.setTransparancy(230);
        
        
        startButton.drawText("START",0,34, 30);
        load.drawText("LOAD",0,34, 30);
        settings.drawText("SETTINGS",0,34, 30);
        quit.drawText("QUIT",0,34, 30);
        
        
        
        addObject(startButton,SCREEN_WIDTH/2,150);
        addObject(load,SCREEN_WIDTH/2,290);
        addObject(settings,SCREEN_WIDTH/2,430);
        addObject(quit,SCREEN_WIDTH/2,570);
        addObject(new Effect(Effect.ROCK, island), SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        addObject(new Effect(Effect.PULSE, clouds), SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
    }

    public void started()
    {
        TitleScreenMusic.playLoop();
    }

    public void stopped()
    {
        TitleScreenMusic.pause();
    }
}
