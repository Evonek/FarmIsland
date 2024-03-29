import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;

/**
 * Super Class for all plants that can be placed on dirtTiles
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Plant extends Tile
{
    protected ObjectID ID;

    protected int growthRate;
    protected int maturity; 
    protected int growthStage;
    protected int lifeTime;
    protected boolean mature;
    protected int yield;
    protected int sellPrice;

    //[Growth Stage][Animation Frame];
    protected GreenfootImage[][] growthAnimations;
    protected int animationIndex;
    protected int deltaIndex;
    protected DirtTile myTile;
    protected HashMap<Integer, Integer> yOffsets;
    protected SimpleTimer actTimer;

    public Plant(){
        this(0); 
    }

    public Plant(int offset){
        super(offset);
        myTile = null;
        myPlot = null;
        deltaIndex = 1;
        lifeTime = 0;
        growthStage = 0;
        maturity = 0;
        mature = false;
        //nextFrame would be 0
        animationIndex = -1;

        actTimer = new SimpleTimer();
        yOffsets = new HashMap<>();
    }

    public void act()
    {
        super.act();
        if(getWorld() == null){
            return;
        }
        
        // 1/60 of a second is 16.666 milliseconds
        if(actTimer.millisElapsed() >= 17){
            actTimer.mark();
            if(myTile != null){
                if(myTile.getWorld() != null){
                    setLocation(myTile.getX(), myTile.getY() + yOffsets.get(growthStage) + myTile.getTileYOffset()/2);
                    if(!mature){
                        grow();
                    }
                }
                else{
                    //collects if mature returns seed if not
                    myTile.unPlant();
                    if(mature){
                        CollectionHandler.collect(this);
                    }
                    else{
                        Inventory.add(ID.getSeedID());
                    }
                    if(getWorld() != null){
                        getWorld().removeObject(this);
                    }

                    return;
                }

                return;
            }
            //animate
            if(lifeTime % 12 == 0 && growthStage >= 0){
                nextFrame();
            }
            //fade
            if(growthStage >= 0){
                fadeOval(getImage());
                lifeTime++;
            }
        }

    }

    public abstract void grow();

    public void checkMouseAction(){
        if(hoveringThis() && Cursor.leftClicked()){
            if(mature){
                CollectionHandler.collect(this);
            }
            else{
                CollectionHandler.shovel(this);
            }
        }
    }
    
    public void plant(LandPlot plot, DirtTile tile){
        myPlot = plot;
        myTile = tile;
        plot.zSort();
    }
    
    
    public abstract void nextFrame();

    public GreenfootImage[] getSeedImages(){
        return growthAnimations[0];
    }

    public void setYOffset(int growthStage, int pixels){
        yOffsets.put(growthStage, pixels);
    }
    //getters and setters
    public int getSellPrice(){
        return sellPrice;
    }

    public int getGrowthRate(){
        return growthRate;
    }

    public void setGrowthRate(int growthRate){
        this.growthRate = growthRate;
    }

    public ObjectID getID(){
        return ID;
    }

    public DirtTile getTile(){
        return myTile;
    }

    public int getMaturity(){
        return maturity;
    }

    public void setMaturity(int maturity){
        this.maturity = maturity;
    }

    public int getGrowthStage(){
        return growthStage;
    }

    public void setGrowthStage(int growthStage){
        if(this.growthStage > growthStage && mature){
            mature = false;
        }
        this.growthStage = growthStage;
    }

    public int getLifeTime(){
        return lifeTime;
    }

    public void setLifeTime(int lifeTime){
        this.lifeTime = lifeTime;
    }

    public boolean isMature(){
        return mature;
    }

    public void setMature(boolean mature){
        this.mature = mature;
    }

    public int getYield(){
        return yield;
    }

    public void setYield(int yield){
        this.yield = yield;
    }

    public DirtTile getDirtTile(){
        return myTile;
    }

    public void setDirtTile(DirtTile newTile){
        myTile = newTile;
    }
}
