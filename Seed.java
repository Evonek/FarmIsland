import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Seed here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Seed extends Item
{
    public static int totalPlantCount;
    private boolean disapearWhenEmpty;

    private GreenfootImage seedImage;
    //is this item a display item or item in play
    private boolean display;
    private int frame;
    private int actCounter;

    private GreenfootSound[] plantingSound;
    private int soundIndex;
    public static int gameVolumeMax = 100; 

    public Seed(ObjectID ID, int amount, boolean disapear){
        disapearWhenEmpty = disapear;
        this.ID = ID;
        Inventory.add(ID, amount);

        seedImage = ID.getDisplayImage();
        double ratio = (double) seedImage.getHeight()/seedImage.getWidth();

        seedImage.scale(32,(int)(32 * ratio + 0.5));

        display = false;
        setImage(seedImage);

        plantingSound = new GreenfootSound[20];
        for (int i = 0; i < plantingSound.length; i++){
            plantingSound[i] = new GreenfootSound ("PlantingSeed.wav");
            plantingSound[i].setVolume(gameVolumeMax);
        }
        soundIndex = 0;
    }

    public Seed(ObjectID ID, int amount){
        this(ID, amount, true);
    }

    public Seed(ObjectID ID){
        this.ID = ID;
        seedImage = ID.getDisplayImage();
        double ratio = (double) seedImage.getHeight()/seedImage.getWidth();
        seedImage.scale(32,(int)(32 * ratio + 0.5));
        display = false;
        setImage(seedImage);
        plantingSound = new GreenfootSound[20];
        for (int i = 0; i < plantingSound.length; i++){
            plantingSound[i] = new GreenfootSound ("PlantingSeed.wav");
            plantingSound[i].setVolume(80);
        }
        soundIndex = 0;
    }

    public void addedToWorld(World w){
        super.addedToWorld(w);

    }

    public void act()
    {
        if(getWorld() == null){
            return;
        }
        actCounter++;
        reposition();
        checkMouseAction();
        animate();

    }

    public Plant newPlant(){
        //add new plants here
        Plant plant = null;
        switch(ID){
            case WHEAT_SEED:
                plant = new Wheat();
                break;
            case PORCUS_WHEAT_SEED:
                plant = new PorcusWheat();
                break;
            case CARROT_SEED:
                plant = new Carrot();
                break;  
            case TOMATO_SEED:
                plant = new Tomato();
                break;   
            case GOLDEN_TOMATO_SEED:
                plant = new GoldenTomato();
                break; 
            case SILVER_TOMATO_SEED:
                plant = new SilverTomato();
                break;  
            case DRAGONFRUIT_SEED:
                plant = new Dragonfruit();
                break;
            case STRAWBERRY_SEED:
                plant = new Strawberry();
                break;
            case BLUEBERRY_SEED:
                plant = new Blueberry();
                break;   
        }
        return plant;
    }

    /**
     * NOTE:
     * animations may not be accurate to what is wanted change if nessesary
     */
    public void animate(){
        //fill later
    }

    public void checkMouseAction(){
        GameWorld myWorld = (GameWorld) getWorld();
        if(!myWorld.isScreen(GameWorld.GAME)){
            return;
        }
        /*if(Greenfoot.mousePressed(null) && hoveringThis()){
            System.out.println("pickedup");
            Cursor.pickUp(this);
        }*/
        /*if(Greenfoot.mouseClicked(null) && hoveringThis()){
            
            Cursor.release();
        }*/
    }

    /**
     * Plants and returns plant
     * 
     * FIX: messy if statements could simplify
     */
    public Plant plant(LandPlot plot, DirtTile tile){
        //if amount is less than zero there is "infinity" stock
        if(getWorld() == null){
            return null;
        }

        Plant plant = newPlant();

        if(Inventory.getAmount(ID) > 0 || Inventory.getAmount(ID) < 0){

            //remove one count of seed in inventory
            getWorld().addObject(plant, tile.getX(), tile.getY() + tile.getTileYOffset()/2);
            plant.plant(plot, tile);
            plantingSound();
            Inventory.remove(ID);
            if(Inventory.getAmount(ID) == 0 && disapearWhenEmpty){

                getWorld().removeObject(this);

            }
        }
        else{

            return null;

        }
        return plant;
    }

    public boolean hoveringThis(){
        MouseInfo mouse = Cursor.getMouseInfo();
        if(mouse == null){
            return false;
        }

        int leftBound = getX() - getImage().getWidth()/2;
        int rightBound = getX() + getImage().getWidth()/2;
        int topBound = getY() - getImage().getHeight()/2;
        int bottomBound = getY() + getImage().getHeight()/2;

        return mouse.getX() < rightBound && mouse.getX() > leftBound && mouse.getY() < bottomBound && mouse.getY() > topBound;
    }

    public void setDisplay(boolean toggle){
        display = toggle;
    }

    public boolean isDisplayed(){
        return display;
    }
        public void plantingSound(){
        plantingSound[soundIndex].play();
        soundIndex++;
        if(soundIndex == plantingSound.length)
        {
            soundIndex=0;
        }       
    }
    
    public static void setVolumeMax(int newMax)
    {
        gameVolumeMax = newMax;
    }
    public static int getVolumeMax()
    {
        return gameVolumeMax;
    }
}
