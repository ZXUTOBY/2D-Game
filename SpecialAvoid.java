public class SpecialAvoid extends Avoid{
    public static final String AVOID_IMAGE_FILE = "assets/jp.jpeg";

    public SpecialAvoid(){
        this(0, 0);        
    }
    
    public SpecialAvoid(int x, int y){
        super(x, y, AVOID_IMAGE_FILE);  
    }

    public void scroll(){
        setX(getX() - AVOID_SCROLL_SPEED*2);
    }


}