import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GenericItem here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GenericItem extends Item
{
    public static final Color CLEAR = new Color(0,0,0,0);
    private Font sFont;
    private Font lFont;
    private GreenfootImage image;
    private GreenfootImage hover;
    private SuperTextBox name;
    private SuperTextBox amount;
    private int width, height;

    private GreenfootSound hoverSound;
    private GreenfootSound[] clickSound;
    private int soundIndex;
    private boolean mouseOver;
    public static int gameVolumeMax = 100; 

    public GenericItem(ObjectID ID){
        this.ID = ID;

        hoverSound = new GreenfootSound ("HoverSoundmp3.mp3");
        hoverSound.setVolume(gameVolumeMax);
        clickSound = new GreenfootSound[6];
        for(int i = 0; i < clickSound.length; i++){
            clickSound[i] = new GreenfootSound("Clickmp3.mp3");
            clickSound[i].setVolume(gameVolumeMax);
        }

        sFont = new Font("Tekton Pro", true, false,  16);
        lFont = new Font("Tekton Pro", true, false,  20);
        String[] nameSplit = ID.toString().split("_");
        String nameString = "";
        for(int i = 0; i < nameSplit.length; i++){
            nameString += nameSplit[i] + " ";
        }
        name = new SuperTextBox(nameString, CLEAR , Color.BLACK, sFont, true, 128, 0, CLEAR );
        amount = new SuperTextBox(String.valueOf(Inventory.getAmount(ID)), CLEAR, Color.BLACK, lFont, true, 128, 0, CLEAR);
        image = new GreenfootImage(ID.getDisplayImage());
        hover = new GreenfootImage(image);
        double ratio = (double) image.getHeight()/image.getWidth();
        image.scale(56, (int) (56 * ratio + 0.5));
        hover.scale(60, (int) (60 * ratio + 0.5));
        width = 56;
        height = (int) (56 * ratio + 0.5);
        setImage(image);
    }

    public void addedToWorld(World w){

    }

    public void act()
    {
        checkMouseAction();
    }

    public void checkMouseAction(){
        if(hoveringThis() && Greenfoot.mouseClicked(null) && Cursor.leftClicked()){
            clickSound();
            Inventory.equipItem(ID);
            Inventory.feedItem(ID);
        }

        if(hoveringThis() != mouseOver){
            mouseOver = ! mouseOver;
            if (mouseOver) // hover begins?
            {
                hoverSound.play();
            }
        }
        World w = getWorld();

        if(hoveringThis()){

            w.addObject(name, getX(), getY() -40);
            amount.update(String.valueOf(Inventory.getAmount(ID)));
            w.addObject(amount, getX() + 28, getY() + 32);
            setImage(hover);
        }
        else{
            w.removeObject(amount);
            w.removeObject(name);
            setImage(image);
        }    
    }

    public boolean hoveringThis(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse != null){

            int leftBound = getX() - width/2;
            int rightBound = getX() + width/2;
            int topBound = getY() - height/2;
            int bottomBound = getY() + height/2;
            return mouse.getX() < rightBound && mouse.getX() > leftBound && mouse.getY() < bottomBound && mouse.getY() > topBound;
        }
        return false;
    }

    public void clickSound()
    {
        clickSound[soundIndex].play();
        soundIndex++;
        if (soundIndex == clickSound.length){
            soundIndex = 0;
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
