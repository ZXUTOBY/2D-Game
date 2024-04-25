public class TrophyGet extends Get{
    
    //Location of image file to be drawn for a SpecialGet
    public static final String Trophy_IMAGE_FILE = "assets/trophy.jpeg";
    
    public TrophyGet(){
        this(0, 0);        
    }
    
    public TrophyGet(int x, int y){
        super(x, y, Trophy_IMAGE_FILE);  
    }
    
    public int getPoints(){
        return 300;
    }

    public void scroll(){
        setX(getX() - GET_SCROLL_SPEED*4);
    }
    
}