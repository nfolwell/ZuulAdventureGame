/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room auditoriumLobby, centerWestHallway, centerEastHallway, fortGreenePlace,
             toNorthWestEntrance, toSouthWestEntrance, auditorium, toNorthEastEntrance,
             toSouthEastEntrance, southEliot, murral, secretRoomBelowAuditorium;
      
        // create the rooms
        secretRoomBelowAuditorium = new Room("secret room below the auditorium");
        auditoriumLobby = new Room("in lobby outside the auditorium");
        centerWestHallway = new Room("in the center west hallway");
        centerEastHallway = new Room("in the center east hallway");
        fortGreenePlace = new Room("outside center west on Fort Greene Place");
        toNorthWestEntrance = new Room("looking toward the north west entrance");
        toSouthWestEntrance = new Room("looking toard the south west entrance");
        auditorium = new Room("Auditorium");
        toNorthEastEntrance = new Room("looking toward the north east entrance");
        toSouthEastEntrance = new Room("looking toward the south east entrance");
        southEliot = new Room("outside center east on South Elliot"); 
        murral = new Room("at the murral in the lobby");
        auditorium = new Room("in the auditorium");
        
        // initialise room exits (north, east, south, west)
        auditoriumLobby.setExits(murral, centerEastHallway, auditorium, centerWestHallway);
        centerWestHallway.setExits(toNorthWestEntrance, auditoriumLobby, toSouthWestEntrance, fortGreenePlace);
        centerEastHallway.setExits(toNorthEastEntrance, southEliot, toSouthEastEntrance, auditoriumLobby);

        fortGreenePlace.setExits(null, centerWestHallway, null, null);
        toNorthWestEntrance.setExits(null, null, centerWestHallway, null);
        toSouthWestEntrance.setExits(centerWestHallway, null, null, null);
        auditorium.setExits(auditoriumLobby, null, null, null);
        murral.setExits(null, null, auditoriumLobby, null);
        southEliot.setExits(null, null, null, centerEastHallway);
        toNorthEastEntrance.setExits(null, null, centerEastHallway, null);
        toSouthEastEntrance.setExits(centerEastHallway, null, null, null);
        
        auditorium.setExit("downstairs",secretRoomBelowAuditorium);
        secretRoomBelowAuditorium.setExit("upstairs",auditorium);
        

        currentRoom = auditoriumLobby;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    public void printLocationInfo()
    {
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        System.out.print(currentRoom.getExitString());
        System.out.println();
    }
    
    

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        nextRoom = currentRoom.getExit(direction);
     
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
           // System.out.println("You are " + currentRoom.getDescription());
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
