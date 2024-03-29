import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.HashMap;
import java.util.ArrayList;
/**
 * Write a description of class GameWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
/**
 * CHANGES:
 * - GameWorld
 * - new Util class
 * - LandPlot
 * - DirtTile
 * - Plant
 * - Tile
 * - Seed
 * - new class Grid Comparator
 * - new class GridPath
 * - Stubby Wheat
 * - Wheat
 * - new class Effect
 * 
 */
public class GameWorld extends World
{
    //720p resolution 16:9 aspect ratio
    private static boolean editMode;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int ACTS_TO_HOME = 30;
    public static final String GAME = "Game";
    public static final String SHOP = "Shop";
    public static final String ACHIEVEMENT = "Achievement";

    private int previousY;

    private LandPlot landPlot;
    private EquipDisplay equip;
    private Button homeButton;
    private boolean shouldMove;
    private double exactDistancePerFrame;
    private int i;
    private double distance;
    //used to indicate which screen is currently in priority/active
    private String screen;
    private ShopMenu shop;
    private Button openShop;
    private AchievementMenu achievement;
    private Button openAchievement;
    private Button leave;

    private AchievementManager achievementManager;

    private Button openInventory;
    private InventoryDisplay inventoryDisplay;
    private Button openPorcus;
    private PorcusMenu porcus;
    
    private SimpleTimer actTimer;
    private SimpleTimer cloudTimer;

    private GreenfootSound GamePlayMusic;
    private GreenfootSound ShopMusic;
    
    //for keypress only
    private boolean inventoryMoving;
    
    public static int gameVolumeMax = 100; 
    /**
     * Constructor for objects of class GameWorld.
     * 
     */
    public GameWorld(String savedFile)
    {    
        
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false); 
        GamePlayMusic = new GreenfootSound ("GamePlayMusic.mp3");
        GamePlayMusic.setVolume(gameVolumeMax);
        ShopMusic = new GreenfootSound ("ShopMusic.mp3");
        ShopMusic.setVolume(gameVolumeMax);
        
        
        initialize(savedFile);
    }

    public void act(){
        
        if(screen.equals(GAME)){
            checkMouseAction();
            checkKeyAction();
            if(actTimer.millisElapsed() >= 17){
                actTimer.mark();
                homeIslands();
                spawnClouds();
            }
            
        }
        
        
        

    }
    public void initialize(String savedFile){
        editMode = true;

        actTimer = new SimpleTimer();
        cloudTimer = new SimpleTimer();
        
        CurrencyHandler.initialize(savedFile);
        CollectionHandler.initialize(this);
        
        setBackground(new GreenfootImage("BackGrounds/Game BG.png"));
        //initializes starting screen

        screen = GAME;
        setPaintOrder(SuperTextBox.class, AchievementNotification.class, CurrencyHandler.class, Item.class, Button.class, ItemFrame.class, AchievementBanner.class, ShopMenu.class, AchievementMenu.class, PorcusMenu.class, InventoryDisplay.class, Fertilizer.class, Plant.class, DirtTile.class, LandPlot.class);

        landPlot = new LandPlot();
        addObject(landPlot, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        
        // Initializes shop menu
        HashMap<ObjectID, Integer> temp = new HashMap<>();
        temp.put(ObjectID.DIRT_TILE, -1);
        temp.put(ObjectID.WHEAT_SEED, -1);
        temp.put(ObjectID.PORCUS_WHEAT_SEED, -1);
        temp.put(ObjectID.FERTILIZER, -1);
        temp.put(ObjectID.CARROT_SEED, -1);
        temp.put(ObjectID.TOMATO_SEED, -1);
        shop = new ShopMenu(temp);

        // Initializes buttons
        openShop = new GameButton("Shop");
        homeButton = new GameButton("Home");
        openAchievement = new GameButton("Achievement");
        openInventory = new GameButton("Inventory");
        leave = new GameButton("Leave");
        openPorcus = new MenuButton("Porcus H");
        porcus = new PorcusMenu(openPorcus);
        
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(homeButton);
        buttons.add(openShop);
        buttons.add(openInventory);
        buttons.add(openAchievement);
        buttons.add(leave);
        inventoryDisplay = new InventoryDisplay(buttons);
        addObject(inventoryDisplay, SCREEN_WIDTH, SCREEN_HEIGHT/2);
        //addObject(porcus, 0, SCREEN_HEIGHT/2);
        equip = new EquipDisplay();
        // Set up the inventory from the previous save
        Inventory.initialize(savedFile, inventoryDisplay, equip);
        Inventory.add(ObjectID.SHOVEL, 1);
        Inventory.add(ObjectID.DIAMOND_TOOL, 1);

        // Initializes achievement menu
        achievement = new AchievementMenu();
        achievementManager = new AchievementManager();

        
        Tool tool = new Tool(ObjectID.DIAMOND_TOOL);
        equip.equipSeed(new Seed(ObjectID.WHEAT_SEED, 1, false));
        equip.equipTool(tool);
        addObject(equip, SCREEN_WIDTH/2, SCREEN_HEIGHT - 64);

        addObject(new CurrencyHandler(), 100, 100);
        //addObject(new Seed(ObjectID.WHEAT_SEED, 1, false), 1200, 650);
        //addObject(new Seed(ObjectID.STUBBY_WHEAT_SEED, 0, false), 1100, 650);

        fillClouds();

    }
    public void fillClouds(){
        for(int i = 0; i < 10; i++){
            int cloudNum = Greenfoot.getRandomNumber(6) + 1;
            int startY = (Greenfoot.getRandomNumber(12) + 1) * SCREEN_HEIGHT/12;
            int startX = (Greenfoot.getRandomNumber(12) + 1) * SCREEN_WIDTH/12;
            while(startY < previousY + 32 && startY > previousY - 32){
                startY = (Greenfoot.getRandomNumber(12) + 1) * SCREEN_HEIGHT/12;
            }
            GreenfootImage cloud = new GreenfootImage("BackGrounds/Cloud " + cloudNum + ".png");
            addObject(new Effect(Effect.SLIDE,cloud, SCREEN_WIDTH + cloud.getWidth() - startX, 1.0/(Greenfoot.getRandomNumber(4) + 1.0)), startX, startY);
        }
    }
    public void spawnClouds(){

        if(cloudTimer.millisElapsed() > 4000){
            cloudTimer.mark();
            int cloudNum = Greenfoot.getRandomNumber(6) + 1;
            int startY = (Greenfoot.getRandomNumber(12) + 1) * SCREEN_HEIGHT/12;
            while(startY < previousY + 16 && startY > previousY - 16){
                startY = (Greenfoot.getRandomNumber(12) + 1) * SCREEN_HEIGHT/12;
            }
            GreenfootImage cloud = new GreenfootImage("BackGrounds/Cloud " + cloudNum + ".png");
            addObject(new Effect(Effect.SLIDE,cloud, SCREEN_WIDTH + cloud.getWidth() * 2, 1.0/(Greenfoot.getRandomNumber(4) + 1.0)), -cloud.getWidth(), startY);
        }

    }
    public void homeIslands(){
        DirtTile centreTile = landPlot.getTile(LandPlot.STARTING_ROW, LandPlot.STARTING_COL);
        int centreX = SCREEN_WIDTH/2;
        int centreY = SCREEN_HEIGHT/2;
        int centreTileX = centreTile.getX();
        int centreTileY = centreTile.getY();
        double deltaX = centreX - centreTileX;
        double deltaY = centreY - centreTileY;
        distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        if(i == ACTS_TO_HOME){
            shouldMove = false;
            i = 0;
        }

        if(shouldMove)
        {
            if(i == 0){
                exactDistancePerFrame = distance/ACTS_TO_HOME;

                centreTile.enableStaticRotation();
                centreTile.turnTowards(centreX, centreY);
                double rotation = centreTile.getPreciseRotation();

                for (Tile object : getObjects(DirtTile.class)) {
                    object.enableStaticRotation();
                    object.setRotation(rotation);
                }

            }
            for (Tile object : getObjects(DirtTile.class)) {
                object.move(exactDistancePerFrame);
            }

            i++;
        }
    }
    
    public void checkKeyAction(){
        
        if(Greenfoot.isKeyDown("b")){
            setScreen(SHOP);
        }
        if(!inventoryDisplay.isMoving() && (Greenfoot.isKeyDown("i") || Greenfoot.isKeyDown("e"))){
            if(inventoryDisplay.isOpen()){
                inventoryDisplay.close();
            }
            else{
                inventoryDisplay.open();
            }
        }
        
    }
    
    public void checkMouseAction(){
        if(openShop.leftClickedThis()){
            setScreen(SHOP);
        }
        if(openAchievement.leftClickedThis()){
            setScreen(ACHIEVEMENT);
        }
        if(homeButton.leftClickedThis() && !shouldMove && distance  != 0){
            shouldMove = true;
        }
        if(openInventory.leftClickedThis()){
            if(inventoryDisplay.isOpen()){
                inventoryDisplay.close();
            }
            else{
                inventoryDisplay.open();
            }
        }    
        if(leave.leftClickedThis()){
            GameInfo.saveGame(this);
        }
        if(openPorcus.leftClickedThis()){
            if(porcus.isOpen()){
                porcus.close();
            }
            else{
                porcus.open();
            }
        }
    }

    public void removeButtons(){
        inventoryDisplay.forceClose();
    }

    public void resetButtons(){
        inventoryDisplay.addButtons();
    }

    public static boolean getEditMode(){
        return editMode;
    }
    //sets edit mode
    public static void setEditMode(boolean mode){
        editMode = mode;
    }
    //changes edit mode from true to false and vice verca
    public static void toggleEditMode(){
        editMode ^= true;
    }

    public boolean isScreen(String screen){
        return this.screen.equals(screen);
    }

    public String getScreen(){
        return screen;
    }

    public void setScreen(String screen){
        this.screen = screen;
        switch(screen){
            case GAME:
                resetButtons();
                equip.showDisplay();
                ShopMusic.stop();
                //addObject(Cursor.getTool(), mouse.getX(), mouse.getY());
                break;
            case SHOP:
                removeButtons();
                equip.hideDisplay();
                GamePlayMusic.stop();
                ShopMusic.play();                
                //removeObject(Cursor.getTool());
                addObject(shop, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
                break;
            case ACHIEVEMENT:
                removeButtons();
                addObject(achievement, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
                break;
        }
    }
    
    public void started()
    {
        GamePlayMusic.playLoop();
    }
    
    public void stopped()
    {
        GamePlayMusic.pause();
        ShopMusic.pause();
    }
    
    public static void setVolumeMax(int newMax)
    {
        gameVolumeMax = newMax;
    }
}
