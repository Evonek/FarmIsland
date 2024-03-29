import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Acesses the universal inventory
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InventoryDisplay extends SuperSmoothMover
{
    public static final int SPEED = 8;
    public static final int BUTTON_SPACING = 16;
    public static final int BUTTON_OFFSET = 32; 
    public static final int ITEM_SPACING = 82;
    public static final int LINE_SPACING = 100;
    public static final int MARGIN = 128;
    public static final int PAGE_SIZE = 15;

    public ArrayList<ArrayList<GenericItem>> pages;
    private GreenfootImage background;
    private ArrayList<Button> buttons;
    private int direction;
    private boolean open;
    private int page;
    private Button nextPage;
    private Button prevPage;
    private SimpleTimer actTimer;
    public InventoryDisplay(ArrayList<Button> buttons){
        this.buttons = buttons;
        initialize();

    }

    public void addedToWorld(World w){

        setLocation(GameWorld.SCREEN_WIDTH + background.getWidth()/2, GameWorld.SCREEN_HEIGHT/2);
        int index = 0;
        for(Button b : buttons){
            w.addObject(b, getX() - background.getWidth()/2 - BUTTON_OFFSET, MARGIN + index * 64 + BUTTON_SPACING);
            index++;
        }

        w.addObject(nextPage, getX() + 130, getY() - 290);
        w.addObject(prevPage, getX() - 110, getY() - 290);
        

    }

    public void act()
    {
        if(actTimer.millisElapsed() >= 4){
            actTimer.mark();
            if(direction == 1){
                open();
            }
            if(direction == -1){
                close();
            }
        }

    }

    public void initialize(){
        pages = new ArrayList<>();
        
        
        background = new GreenfootImage("Backgrounds/inventory.png");
        nextPage = new MenuButton("Inventory Right");
        prevPage = new MenuButton("Inventory Left");
        
        Font font = new Font("Tekton Pro", true, false,  30);
        SuperTextBox box = new SuperTextBox("INVENTORY", new Color(0,0,0,0), Color.BLACK, font, true, background.getWidth(), 0, null);
        
        background.drawImage(box.getImage(), 10,24);
        actTimer = new SimpleTimer();
        setImage(background);
    }

    public boolean isOpen(){
        return open;
    }
    
    public boolean isMoving(){
        return direction != 0;
    }
    public void removeItem(ObjectID ID){
        ArrayList<GenericItem> items = pages.get(page);
        for(Item i : items){
            if(ID.equals(i.getID())){
                items.remove(i);
                getWorld().removeObject(i);
                break;
            }
        }
        System.out.println(pages.get(page).size());
        reloadItems();
    }

    public void addItem(ObjectID ID){
        if(pages.isEmpty()){
            page = 0;
            ArrayList<GenericItem> page = new ArrayList<>();
            page.add(new GenericItem(ID));
            pages.add(page);
        }
        else if(pages.get(pages.size() - 1).size() < PAGE_SIZE){
            pages.get(page).add(new GenericItem(ID));
        }
        else{
            ArrayList<GenericItem> page = new ArrayList<>();
            page.add(new GenericItem(ID));
            pages.add(page);
        }
        reloadItems();
    }

    public void reloadItems(){
        int startX = getX() - 74;
        int startY = getY() - 182;
        int index = 0;
        for(Item i : pages.get(page)){
            int row = index/3;
            int col = index % 3;
            int x = startX + col * ITEM_SPACING;
            int y = startY + row * LINE_SPACING;
            if(i.getWorld() == null){
                getWorld().addObject( i, x, y);
            }
            else{
                i.setLocation(x, y);
            }
            index++;
        }
    }

    public void forceClose(){
        setLocation(GameWorld.SCREEN_WIDTH + background.getWidth()/2, getY());
        for(Button b : buttons){
            getWorld().removeObject(b);
        }
        nextPage.setLocation(getX() + 130, getY() - 290);
        prevPage.setLocation(getX() - 110, getY() - 290);
        reloadItems();
        open = false;
    }

    public void addButtons(){
        int index = 0;
        for(Button b : buttons){
            getWorld().addObject(b, getX() - background.getWidth()/2 - BUTTON_OFFSET, MARGIN + index * 64 + BUTTON_SPACING);
            index++;
        }

    }

    public void forceOpen(){
        setLocation(GameWorld.SCREEN_WIDTH - background.getWidth()/2, getY());
        for(Button b : buttons){
            b.setLocation(getX() - background.getWidth()/2 - BUTTON_OFFSET, b.getY());
        }
        open = true;
    }

    public void open(){
        open = true;
        direction = 1;
        if(getX() > GameWorld.SCREEN_WIDTH - background.getWidth()/2){
            setLocation(getX() - SPEED, getY());
            for(Button b : buttons){
                b.setLocation(b.getX() - SPEED, b.getY());
            }
            for(Item i : pages.get(page)){
                i.setLocation(i.getX() - SPEED, i.getY());
            }
            nextPage.setLocation(nextPage.getX() - SPEED, nextPage.getY());
            prevPage.setLocation(prevPage.getX() - SPEED, prevPage.getY());
        }
        else{
            direction = 0;
            setLocation(GameWorld.SCREEN_WIDTH - background.getWidth()/2, getY());
            for(Button b : buttons){
                b.setLocation(getX() - background.getWidth()/2 - BUTTON_OFFSET, b.getY());
            }
            reloadItems();
            nextPage.setLocation(getX() + 130, getY() - 290);
            prevPage.setLocation(getX() - 110, getY() - 290);
        }
    }

    public void close(){
        open = false;
        direction = -1;
        if(getX() < GameWorld.SCREEN_WIDTH + background.getWidth()/2){
            setLocation(getX() + SPEED, getY());
            for(Button b : buttons){
                b.setLocation(b.getX() + SPEED, b.getY());
            }
            for(Item i : pages.get(page)){
                i.setLocation(i.getX() + SPEED, i.getY());
            }
            nextPage.setLocation(nextPage.getX() + SPEED, nextPage.getY());
            prevPage.setLocation(prevPage.getX() + SPEED, prevPage.getY());
        }
        else{
            direction = 0;
            setLocation(GameWorld.SCREEN_WIDTH + background.getWidth()/2, getY());
            for(Button b : buttons){
                b.setLocation(getX() - background.getWidth()/2 - BUTTON_OFFSET, b.getY());
            }
            reloadItems();
            nextPage.setLocation(getX() + 130, getY() - 290);
            prevPage.setLocation(getX() - 110, getY() - 290);
        }
    }
}
