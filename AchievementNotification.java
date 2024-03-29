import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AchievementNotification here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AchievementNotification extends SuperSmoothMover
{
    public static int WIDTH = 255;
    public static int HEIGHT = 44;
    private GreenfootImage image;
    private Achievement achievement;
    
    public AchievementNotification(Achievement achievement){
        this.achievement = achievement;
        initialize();
        setImage(image);
    }
    
    public void initialize(){
        image = new GreenfootImage("Displays/AchievementBanners/" + achievement.getNotificationIcon() + ".png");
    }
}
