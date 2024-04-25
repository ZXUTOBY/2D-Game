import java.awt.*;
import java.awt.event.*;
import java.util.*;
//The basic ScrollingGame, featuring Avoids, Gets, and SpecialGets
//Players must reach a score threshold to win
//If player runs out of HP (via too many Avoid collisions) they lose
public class ScrollingGame extends GameEngine {
    
 
    
    //Starting Player coordinates
    protected static final int STARTING_PLAYER_X = 0;
    protected static final int STARTING_PLAYER_Y = 100;
    
    //Score needed to win the game
    protected static final int SCORE_TO_WIN = 30;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    protected static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    protected static final int SPEED_CHANGE = 20;    
    
    protected static final String INTRO_SPLASH_FILE = "assets/steph.png";        
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    protected static final int SPAWN_INTERVAL = 45;
    
    
    //A Random object for all your random number generation needs!
    protected static final Random rand = new Random();
    
            
    
    //Player's current score
    protected int score;
    
    //Stores a reference to game's Player object for quick reference
    //(This Player will also be in the displayList)
    protected Player player;

    protected boolean win;
    
    
    
    
    
    public ScrollingGame(){
        super();
    }
    
    public ScrollingGame(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void pregame(){
        this.setBackgroundImage("assets/chase_center.png");
        this.setSplashImage(INTRO_SPLASH_FILE);
        player = new Player(STARTING_PLAYER_X, STARTING_PLAYER_Y);
        displayList.add(player); 
        score = 0;
    }
    
    
    //Called on each game tick
    protected void updateGame(){
        //scroll all scrollable Entities on the game board
        scrollEntities();   
        //Spawn new entities only at a certain interval
        if (super.getTicksElapsed() % SPAWN_INTERVAL == 0){            
            spawnEntities();
        }
        gcOutOfWindowEntities();
        PlayerCollision();
        
        //Update the title text on the top of the window
        setTitleText("HP: " + player.getHP() + "    score: " + score);        
    }
    
    
    //Scroll all scrollable entities per their respective scroll speeds
    protected void scrollEntities(){
        for (Entity i: displayList){
           if (i instanceof Avoid){
            ((Avoid)i).scroll();
           }
           if (i instanceof Get){
            ((Get)i).scroll();
           }
            //****  finish me!  ****  
            //How do you know which Entities to scroll?
           
        }
    }
    
    
    //Handles "garbage collection" of the displayList
    //Removes entities from the displayList that have scrolled offscreen
    //(i.e. will no longer need to be drawn in the game window).
    protected void gcOutOfWindowEntities(){
        Iterator<Entity> iterator = displayList.iterator();
        while (iterator.hasNext()){
            Entity item = iterator.next();
            if (item.getX()+item.getWidth()<0) iterator.remove();
        }
        //****   implement me!   ****
        //also: this function is not currently being called anywhere
        //where should it be called?
       
    }
    
    
    //Called whenever it has been determined that the Player collided with a collectable
    protected void handlePlayerCollision(Collectable collidedWith){
        if (collidedWith instanceof SpecialAvoid && player.getMovementSpeed()>2){
            player.setMovementSpeed(player.getMovementSpeed()-2);
        }
        player.modifyHP(collidedWith.getDamage());
        score += collidedWith.getPoints();
        displayList.remove((Entity)collidedWith);


    }


    protected void PlayerCollision(){
        for (Entity i : checkCollision(player)){
            handlePlayerCollision((Collectable)i);
        }
    }
    
    //Spawn new Entities on the right edge of the game window
    protected void spawnEntities(){
        int rand_num = rand.nextInt(6)+1;
        for (int i = 0; i < rand_num; i++){
            int rand_in_ten = rand.nextInt(100);
            Entity new_entity;
            if (rand_in_ten<20) new_entity = new Get(800, rand.nextInt(500));
            else if (20<=rand_in_ten && rand_in_ten<75) new_entity = new Avoid(800, rand.nextInt(500));
            else if (75<=rand_in_ten && rand_in_ten<88) new_entity = new SpecialGet(800, rand.nextInt(500));
            else if (88<=rand_in_ten && rand_in_ten<99) new_entity = new SpecialAvoid(800, rand.nextInt(500));
            else new_entity = new TrophyGet(800, rand.nextInt(500));

            if (checkCollision(new_entity).size()==0) displayList.add(new_entity);
        }
        //****   implement me!   ****
            
    }
    
    
    //Called once the game is over, performs any end-of-game operations
    protected void postgame(){
        if (win) {super.setTitleText("You have already scored 30 points! You won!"); setBackgroundImage("assets/steph2.gif");}
        else {super.setTitleText("You are cooked, trash"); setBackgroundImage("assets/lose.gif");}
    }
    
    
    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    protected boolean determineIfGameIsOver(){
        if (score >= SCORE_TO_WIN) { win = true; return true;}
        if (!(player.getHP()>0)) {win = false; return true;}
        return false;   //****   placeholder... implement me!   ****
       
    }
    
    
    
    //Reacts to a single key press on the keyboard
    protected void reactToKey(int key){
        
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key) + ",  DisplayList size: " + displayList.size());
        
        //if a splash screen is active, only react to the "advance splash" key... nothing else!
        if (getSplashImage() != null){
            if (key == ADVANCE_SPLASH_KEY)
                super.setSplashImage(null);
            return;
        }   
        // pause game
        if (key == KEY_PAUSE_GAME){
            isPaused = !isPaused;
            return;
        }
        // up down left right
        for (int i : MOVEMENT_KEYS) if (key == i){
        if (key == UP_KEY && player.getY() >= 0) {player.setY(player.getY()-player.getMovementSpeed());}
        if (key == DOWN_KEY && player.getY()+player.getHeight() <= 570) {player.setY(player.getY()+player.getMovementSpeed());}
        if (key == LEFT_KEY && player.getX() >= 5) {player.setX(player.getX()-player.getMovementSpeed());}
        if (key == RIGHT_KEY && player.getX()+player.getWidth() <= 895) {player.setX(player.getX()+player.getMovementSpeed());}
        return;
        }
        // speed of the game
        if (key == SPEED_DOWN_KEY){
            if (getGameSpeed()-SPEED_CHANGE > 0) setGameSpeed(getGameSpeed()-SPEED_CHANGE);
            return;
        }
        if (key == SPEED_UP_KEY){
            if (getGameSpeed()+SPEED_CHANGE <= 300) setGameSpeed(getGameSpeed()+SPEED_CHANGE);
            return;
        }






            
    }    
    
    
    //Handles reacting to a single mouse click in the game window
    //Won't be used in Milestone #2... you could use it in Creative Game though!
    protected MouseEvent reactToMouseClick(MouseEvent click){
        if (click != null){ //ensure a mouse click occurred
            int clickX = click.getX();
            int clickY = click.getY();
            setDebugText("Click at: " + clickX + ", " + clickY);
        }
        return click;//returns the mouse event for any child classes overriding this method
    }
    
    
    
    
}
