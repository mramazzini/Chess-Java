import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Graph extends JPanel{
	
	
	
	private final int TileXsize = (int) Math.round(800*0.1);
	private final int TileYsize = TileXsize;
	private int pieceX;
	private int pieceY;
	private final int pixel = 10;
	private Piece[] pieces;
	private int heldX=-2; //Sets coordinates of the heldPiece original location, defaults out of the screen
	private int heldY=-2;
	private int[] legalX = new int[0]; //The legal x coordinates of the heldPiece
	private int[] legalY = new int[0]; //The legal y coordinates of the heldPiece
	private int[] threatX = new int[0]; //The threatened x coordinates of the piece
	private int[] threatY = new int[0];
	
	
	public Graph(int x, int y, Piece[] Piece) {
		
		pieces=Piece;
		
		repaint();
		setLocation(0,0);
		setSize(x,y);
		
	}
	
	public void paint(Graphics g) {
		
		
		//Draw Background
		drawBackground(g);
		
		//Draw Chess board
		drawBoard(g);
		
		
		
		//Draw Legal Moves
		drawLegalMoves(g);
		
		//Draw Selected Tile, if any
		drawPickedTile(g);
		//drawThreatened(g);
		//Draw Pieces
		
		for(int i=0; i<pieces.length; i++) {
			drawPiece(pieces[i],g);
			drawPiece(pieces[i],g);
		}
		
	}
	//Draw The Chess Board
	public void drawBoard(Graphics g) {
		Color Board1 = new Color(150,150,150);
		Color Board2 = new Color(0,0,0);
		Boolean skip=true;
		for (int i =1; i<9; i++) {
			for (int j=1; j<9; j++) {
			
				if(skip) {
					g.setColor(Board1);
					g.fillRect(TileXsize*i,TileYsize*j,TileXsize,TileYsize);
					skip=false;
				}
				else {
					g.setColor(Board2);
					g.fillRect(TileXsize*i,TileYsize*j,TileXsize,TileYsize);
					skip=true;
				}
			
			}
			skip=!skip;
		}
	}
	
	//Draw The Background brown filler
	public void drawBackground(Graphics g) {
		Color Background = new Color(77,51,0);
		
		//Draw Brown Background
		g.setColor(Background);
		g.fillRect(80,0, 800,800);
		g.fillRect(0,80, 800,800);
	}
	
	//Draw A single x by y pixel (For making piece art)
	public void dp(int x, int y, Graphics g) { 
		g.fillRect(TileXsize*(pieceX+1)+x*pixel, TileYsize*(pieceY+1)+pixel*y,pixel,pixel);
	}
	public void drawThreatened(Graphics g) {
		g.setColor(Color.red);
		for (int i=0; i<threatX.length; i++) {
			System.out.println("kek");
			g.fillRect(TileXsize*(threatX[i]+1),TileYsize*(threatY[i]+1),TileXsize,TileYsize);
		}
	}
	
	public void drawPiece(Piece p, Graphics g) {
		Color pieceWcol = new Color(114,160,193);
		Color pieceBcol = new Color(0,51,17);
		pieceX=p.getX();
		pieceY=p.getY();
		
		if (p.getColor()) {
			g.setColor(pieceWcol);
		}
		else {
			g.setColor(pieceBcol);
		}
		
		//Draw Pawn
		if(p.getType().equals("pawn")) {
			int[] pawnx = {2,3,4,5, 3,4, 2,3,4,5,  3,4}; //Stores the pixel values of the pawn drawing -- Space represents new row
			int[] pawny = {6,6,6,6, 5,5, 4,4,4,4,  3,3};
		
			for(int i=0; i<pawnx.length; i++) {
					dp(pawnx[i],pawny[i],g);
			}
				
		}
		
		//Draw Bishop
		else if (p.getType().equals("bishop")) {
			int[] bishx = {2,3,4,5, 3,4, 3,4, 3,4, 2,3,4,5, 3,4, }; //Stores the pixel values of the bishop drawing
			int[] bishy = {6,6,6,6, 5,5, 4,4, 3,3, 2,2,2,2, 1,1 };
			
			for(int i=0; i<bishx.length; i++) {
					dp(bishx[i],bishy[i],g);
			}
		}
		
		//Draw Rook
		else if(p.getType().equals("rook")) {
			int[] rookx = {1,2,3,4,5,6, 2,3,4,5, 2,3,4,5, 2,3,4,5, 1,2,3,4,5,6, 1,3,4,6}; //Stores the pixel values of the rook drawing 
			int[] rooky = {6,6,6,6,6,6, 5,5,5,5, 4,4,4,4, 3,3,3,3, 2,2,2,2,2,2, 1,1,1,1};
			
			for(int i=0; i<rookx.length; i++) {
					dp(rookx[i],rooky[i],g);
			}
		}
		
		//Draw Knight
		else if(p.getType().equals("knight")) {
			int[] knix = {1,2,3,4,5,6, 2,3,4,5, 3,4,5, 1,2,3,4,5, 1,2,3,4,5, 2,3,4,5, 5}; //Stores the pixel values of the knight drawing 
			int[] kniy = {6,6,6,6,6,6, 5,5,5,5, 4,4,4, 3,3,3,3,3, 2,2,2,2,2, 1,1,1,1, 0};
			
			for(int i=0; i<knix.length; i++) {
					dp(knix[i],kniy[i],g);
			}
		}
		
		//Draw Queen
		else if(p.getType().equals("queen")) {
			int[] queenx = {1,2,3,4,5,6, 2,3,4,5, 3,4, 3,4, 2,3,4,5, 1,3,4,6}; //Stores the pixel values of the queen drawing 
			int[] queeny = {6,6,6,6,6,6, 5,5,5,5, 4,4, 3,3, 2,2,2,2, 1,1,1,1};
			
			for(int i=0; i<queenx.length; i++) {
					dp(queenx[i],queeny[i],g);
			}
		}
		
		//Draw King
		else if(p.getType().equals("king")) {
			int[] kingx = {1,2,3,4,5,6, 2,3,4,5, 2,3,4,5, 3,4, 2,3,4,5, 3,4}; //Stores the pixel values of the queen drawing 
			int[] kingy = {6,6,6,6,6,6, 5,5,5,5, 4,4,4,4, 3,3, 2,2,2,2, 1,1};
			
			for(int i=0; i<kingx.length; i++) {
					dp(kingx[i],kingy[i],g);
			}
		}
		
	}
	
	public void changePiece(Piece piece, int value) {
		pieces[value]=piece;
	}
	public void setSelectedTile(int x, int y){ //Will set the coordinates of the tile being picked up 
		heldX=x;
		heldY=y;
	}
	public void drawPickedTile(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(TileXsize*(heldX+1),TileYsize*(heldY+1),TileXsize,TileYsize);
	}
	public void drawLegalMoves(Graphics g) {
		g.setColor(Color.blue);
		for (int i=0; i<legalX.length; i++) {
			g.fillRect(TileXsize*(legalX[i]+1),TileYsize*(legalY[i]+1),TileXsize,TileYsize);
		}
	}
	public void setLegalX(int[] x) {
		legalX=x;
	}
	public void setLegalY(int[] y) {
		legalY=y;
	}
	public void setThreatX(int[] x) {
		threatX=x;
	}
	public void setThreatY(int[] y) {
		threatY=y;
	}
	public void clearHeldTiles() { //will clear the values of heldPiece
		   setSelectedTile(-2, -2); //Will remove the coordinates of heldPiece, clearing the green square
		   setLegalX(new int[0]); //Will remove the legal moves, clearing the blue squares
		   setLegalY(new int[0]);
		   repaint();
	}
}
