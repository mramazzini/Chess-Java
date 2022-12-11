
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board implements MouseMotionListener, MouseListener, Runnable{
	
	//JFrame and Panel for Main Window.
	private JFrame table;
	
	
	//New Game button (Also resets the console for new command)
	private Button NGame;
	
	//Mouse Variable
	private int mouseX;
	private int mouseY;
	private int mouseTileX;  //mousex and mouseY store the pixel coordinates while mouseTileX and mouseTileY store the chess board coordinates
	private int mouseTileY;
	private boolean mousePressed;
	private MListener MListener;
	private boolean trackMouse;
	
	//Screen dimension 
	
	private final int WIDTH=816;
	private final int HEIGHT=WIDTH+20;
	
	
	//Tiles array
	private Tile[][] Tiles;
	
	//Pieces arrays
	public Piece[] Pieces;  //Black

	
	//Array value of the piece that is currently being held, if any
	private int heldPiece;
	private boolean isHoldingPiece;
	private int heldPieceX; //Holds the coordinates of heldPiece before it was moved
	private int heldPieceY;
	
	private int[] legalX;
	private int[] legalY;
	
	//Graphics 
	private Graph Graphics;
	
	//Console Reader (For debugging)
	private BufferedReader reader;
	
	//True for whites turn, False for blacks turn
	private boolean turn;
	
	
	public Board() {
		
		//Build the JFrame Window and JPanel for background
		buildWindow();
		
		
		//Build Pieces
		buildPieces();
		
		
		//Paint the board
		buildGraphics();
		
		
		//Place Buttons
		buildButtons();
		
		//Build Tiles
		buildTiles();
		
		//Listener
		startMouseListener();
		
		//Run and Debugging
		startConsole();
		calcMoves();
		run();
		
		
	}
	
	

	public void run()//Timer
	   {
	   	try
	   	{
	   		while(true)
	   		{
	   			calcMoves();
	   		   Thread.currentThread().sleep(10);
	           //Insert repeated actions here
	   		  
	   		   if(holdingPiece()) { //If A piece is clicked it will move with the mouse
	   			
	   			   setLegalMoves(Pieces[heldPiece]);  //Determines the legal moves for the currently held piece
	   			   
	   			   heldPieceX=Pieces[heldPiece].getX(); //Hold the original heldPiece coordinates in case player places the piece back to its original position
	   			   heldPieceY=Pieces[heldPiece].getY();
	   			   
	   			   Graphics.setSelectedTile(Pieces[heldPiece].getX(), Pieces[heldPiece].getY()); //Sets the coordinates of the heldPiece into the graphics class 
	   			   Graphics.setLegalX(Pieces[heldPiece].getLegalX()); //Sets the legal moves of heldpiece into the graphics class
	   			   Graphics.setLegalY(Pieces[heldPiece].getLegalY());
	   			   
	   			   
	   			   if(Pieces[heldPiece].getIsMoveable()) { //Determines whether the piece has any legal moves
	   				   
	   				   int skip=0;
		   			   while(mousePressed) {     //Runs until player clicks a legal spot
		   				
		   				
		   				 
		   				if(skip==100000) {
		   					
		   					printMouseInfoHolding();
		   					skip=0;
		   				}
		   				else {
		   					skip++;
		   					
		   				}
		   				movePiece();
		   			   }
		   			if(!(heldPieceX==Pieces[heldPiece].getX() && heldPieceY==Pieces[heldPiece].getY())) { //The user did not place the piece back and played a valid move
		   				
		   				capturePiece(); //Will capture piece if attempting to
		   				castleKing(); //Will castle the king if attempting to
		   				if(Pieces[heldPiece].getType().equals("pawn") && (Pieces[heldPiece].yTile==7 || Pieces[heldPiece].yTile==0)) {
							Pieces[heldPiece].promotePawn();
						}
		   				Pieces[heldPiece].setFirstMove(false);
			   			   
			   			   updateTurn(!turn);
		   			   }
		   			
		   			   
	   			   }
	   			   else { //The User clicked a piece with no legal moves
	   				   Graphics.clearHeldTiles();
	   				   
	   			   }
	   			MListener.setPressed(false);
	   			mousePressed=false;
	   			isHoldingPiece=false; 
	   			Graphics.clearHeldTiles();
	   		   
	   		   }
	   		   printMouseInfo();
	   		 buildButtons(); //Place the button/s over Graphics again
	   		   
	
	            
	         }
	      }
	   		catch(Exception e)
	      {
	      }
	  	}
	
	

	private void startConsole() {  //Console will read commands and output accordingly
		calcMoves();
		clearScreen();
		trackMouse=false;
		System.out.println("Type: trackmouse, tile, screen, start, pieces");
		reader = new BufferedReader(
				new InputStreamReader(System.in));
			String I = "";
			try {
				I = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				  
			
			String p = "px";
			
			//Enable Mouse coordinate tracking; Default trackmouse=false
			
			if(I.equals("trackmouse")) {
				trackMouse=true;
			}
			
			//prints pixel coordinates for the x y values for each tile (top left of square)
			else if(I.equals("tile")){
				for (int i=0; i<8; i++) {
					for (int j=0; j<8; j++) {
						System.out.println("Tile (" + i + "," + j + ") Xval = " +Tiles[i][j].getTileXY()[0]+ p);
						System.out.println("Tile (" + i + "," + j + ") Yval = " +Tiles[i][j].getTileXY()[1]+ p);
					}
				}
			}
			else if(I.equals("screen")) {
				System.out.println("DimensionX = "+WIDTH +p);
				System.out.println("DimensionY = "+HEIGHT+p);
			}
			else if(I.equals("pieces")) {
				System.out.println("Pieces:");
				for(int i=0;i<32;i++) {
					Pieces[i].printPiece();
				}
			}
			else if(I.equals("start")){
				
			}
	}

	private void printMouseInfo() {
		if(trackMouse) {  //Runs if trackmouse is written in console
			clearScreen();
		   System.out.println("     MouseX = "+mouseX);
		   System.out.println("     MouseY = "+mouseY);
		   System.out.println("BoardMouseX = "+mouseTileX);
		   System.out.println("BoardMouseY = "+mouseTileY);
		   System.out.println("     Click  = "+mousePressed);
		   System.out.println("  HoldPiece = "+isHoldingPiece);
		   
			    
			   
		   
		}
	}
	private void printMouseInfoHolding() { //same as printMouseInfo but runs when holding a piece
		if(trackMouse) {  //Runs if trackmouse is written in console
			clearScreen();
		   System.out.println("     MouseX = "+mouseX);
		   System.out.println("     MouseY = "+mouseY);
		   System.out.println("BoardMouseX = "+mouseTileX);
		   System.out.println("BoardMouseY = "+mouseTileY);
		   System.out.println("     Click  = "+mousePressed);
		   System.out.println("  HoldPiece = "+isHoldingPiece);
		   System.out.println(" HeldPieceX = "+Pieces[heldPiece].getX());
		   System.out.println(" HeldPieceY = "+Pieces[heldPiece].getY());
		   System.out.println("isLegalMove = " +detectLegalMove()); 
		   System.out.print(" LegalMoves :");
		   for (int i=0; i<legalX.length; i++) {
		   System.out.print(" ("+legalX[i]+","+legalY[i]+")");
			   }
			    
			   
		   
		}
	}
	
	public static void clearScreen() {  //Reset the console
		System.out.println(System.lineSeparator().repeat(100));
	}
	
	public void buildWindow() { //Build JFrame Window and bakground JPanel
		
		//JFrame (window)
		
		
		table= new JFrame("Chess");
		table.setSize(WIDTH,HEIGHT);
		table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		table.setResizable(false);
		table.setLayout(null);
		table.setVisible(true);
	}
	private void calcMoves() {
		
		for(int i=0; i<Pieces.length; i++) {
			
			Pieces[i].setPieces(Pieces);
		}
		for(int i=0; i<Pieces.length; i++) {
			
			Pieces[i].calcMoves();
		}
	}
	
	private void buildGraphics() { //Initializes Graphics and puts it on JFrame
			
			Graphics= new Graph(WIDTH,HEIGHT, Pieces);
			table.add(Graphics);
			Graphics.repaint();
		}
	
	public void buildButtons() { //Builds the Buttons for the JFrame
		//New Game Button
				NGame = new Button(0,0, 80, 80, "New", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//Actions when New Game button is pressed
						startConsole();
						
					}
				});
				NGame.getButton().setFocusable(true);
				table.add(NGame.getButton());
	}
	
	public void buildPieces() { //Builds the Pieces for the board
		
		//Initializes the Piece array
		Pieces= new Piece[32];
		
		
		int Count=0; 	 	  //Counts how many black pieces there currently are
		boolean swap = false; //Determines which side the piece will be on for placing rooks, bishops, and knights
		
		
		//Create pawns
		for(int i=0; i<8; i++) {
			Pieces[Count]=new Piece(i,1, "pawn", false);
			Count++;
			Pieces[Count]=new Piece(i,6, "pawn", true);	
			
			Count++;
			
		}
	
		//Create Rooks
		int[] r= {0,7}; //initial x values for rooks
		
		for(int i:r) {
			
			Pieces[Count]=new Piece(i,0, "rook", false);
			Count++;
			Pieces[Count]=new Piece(i,7, "rook", true);
			
			Count++;
			
		}
		
		//Create Knights
		int[] k= {1,6}; //initial x values for knights
		for(int i:k) {
			Pieces[Count]=new Piece(i,0, "knight", false);
			Count++;
			Pieces[Count]=new Piece(i,7, "knight", true);
			Count++;
			
		}
		
		//Create Bishops
		int[] b= {2,5}; //initial x values for bishops
		for(int i:b) {
			Pieces[Count]=new Piece(i,0, "bishop", false);
			Count++;
			Pieces[Count]=new Piece(i,7, "bishop", true);
			Count++;
			
		}
		
		//Create Kings
		//Initial x value for Kings is 4
		Pieces[Count]=new Piece(4,0, "king", false);
		Count++;
		Pieces[Count]=new Piece(4,7, "king", true);
		Count++;
		
		
		//Create Queens
		//Initial y value for queens is 3
		Pieces[Count]=new Piece(3,0, "queen", false);
		Count++;
		Pieces[Count]=new Piece(3,7, "queen", true);
		Count++;
		
		//Sets the turn (White goes first)
		
		updateTurn(true);
	}
	
	
	
	public void buildTiles() { //Create JPanels for every tile to put in Tiles[][] array and assign background color to them

		
		Tiles = new Tile[8][8];
		
		
		for (int i =0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
			Tiles[i][j]=new Tile(i,j);
			table.add(Tiles[i][j].getTile());
			
		}
	}
	}
	
	
	private boolean holdingPiece() { //Returns true if the mouse is over a tileXY that contains a piece object and is of this current turn
		for(int i=0;i<Pieces.length; i++) {
			Pieces[i].setPieces(Pieces);
		}
		
		if(mousePressed && tileContainsPiece(mouseTileX, mouseTileY)) {
			if(Pieces[heldPiece].getColor()==turn) {
			isHoldingPiece=true;
			return true;
			}
		}
		//resets mousePressed to false because player clicked blank tile
		
		mousePressed=false;
		MListener.setPressed(false);
		
		return false;
	}

	private boolean tileContainsPiece(int x, int y) { //Returns true if the tile and the xy coordinates contains a piece object. Will also initialize heldPiece as well as create the legal moves
		for (int i=0; i<Pieces.length; i++) {
			if(Pieces[i].getX()==x && Pieces[i].getY()==y) {
				heldPiece=i;
				
				return true;
			}
		}
		
		
		return false;
	}
	
	public void movePiece() {
		if(isHoldingPiece) {
			  
 			  Pieces[heldPiece].movePiece(mouseTileX, mouseTileY);
 			  Graphics.changePiece(Pieces[heldPiece], heldPiece);
 			  Graphics.repaint();
			  
		}
		
	}
	
	public boolean detectLegalMove() { //returns true if the tile the mouse is hovering over is a legal move
		
		for (int i=0; i<Pieces[heldPiece].getLegalX().length;i++) {
			
			if(legalX[i] == mouseTileX && legalY[i] == mouseTileY) {
				
				return true;
			}
		}
		return false;
	}
	
	private void setLegalMoves(Piece piece) { //makes the legal moves equal to that which the piece allows
		
		piece.calcMoves();
		legalX = new int[piece.getLegalX().length];
		legalY = new int[piece.getLegalY().length];
		
		for(int i=0;i<piece.getLegalX().length; i++) {
			legalX[i]=piece.getLegalX()[i];
			
			legalY[i]=piece.getLegalY()[i];
			
		}
	}
	public void castleKing() {
		if(Pieces[heldPiece].getType().equals("king") && Pieces[heldPiece].firstMove) {
			
			for(int i=0; i<Pieces.length; i++) {
				
				
				if(Pieces[i].getColor()==Pieces[heldPiece].getColor() && Pieces[i].getType().equals("rook") && Pieces[i].getX()==Pieces[heldPiece].getX()+1 && Pieces[i].getY()==Pieces[heldPiece].getY()) {
					Pieces[i].setX(Pieces[i].getX()-2);
				}
				else if(Pieces[i].getColor()==Pieces[heldPiece].getColor() && Pieces[i].getType().equals("rook") && Pieces[i].getX()==Pieces[heldPiece].getX()-2 && Pieces[i].getY()==Pieces[heldPiece].getY()) {
					Pieces[i].setX(Pieces[i].getX()+3);
				}
			}
		}
	}
	
	public void startMouseListener() { //Initializes MouseListener and Mouse Motion Listener and adds it to the table
		MListener = new MListener();
		table.addMouseMotionListener(MListener);
		table.addMouseMotionListener(this);
		table.addMouseListener(MListener);
		table.addMouseListener(this);
	}
	
	
	//MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
		
		
		mouseX=MListener.getMouseX();
		mouseY=MListener.getMouseY();
		mouseTileX=(mouseX-8)/80-1;
		mouseTileY=(mouseY-29)/80-1;
		
	}  

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//calcPieceThreat();
		
		if(isHoldingPiece) {
			if(detectLegalMove()) {
				MListener.setIllegalMove(false);
				
				mousePressed=MListener.getPressed();
				mouseX=MListener.getMouseX();
				mouseY=MListener.getMouseY();
			}
			else {
				MListener.setIllegalMove(true);
				
			}
			
		}
		else {
			mousePressed=MListener.getPressed();
			mouseX=MListener.getMouseX();
			mouseY=MListener.getMouseY();
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	public void updateTurn(boolean b) { //Sets the turn of the game
		turn=b;
		for(int i=0; i<Pieces.length; i++) {
			Pieces[i].setTurn(b);
		}
	}
	public void capturePiece() {
		for(int i=0; i<Pieces.length; i++) {
			//If the XY coordinates or heldPiece are equal to the XY coordinates of a piece of opposite color, meaning the player is attempting to capture the oponent piece
			if(Pieces[heldPiece].getX()==Pieces[i].getX() && Pieces[heldPiece].getY()==Pieces[i].getY() && Pieces[heldPiece].getColor()!= Pieces[i].getColor()) { 
				
				//Send piece to the Shadow realm 
				Pieces[i].setCaptured(true);
			}
			
		}
	}
	
	
}



