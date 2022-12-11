import java.util.ArrayList;

public class Piece {
	
	int xTile; //What X Tile its on
	int yTile; //What Y Tile its on
	String type; //What kind of piece
	Boolean color; //True for white, False for black
	Piece[] Pieces; //The current Pieces array from Board class
	boolean isMoveable; //False is there are no legal moves
	boolean turn; //Gets the turn of the game
	boolean captured; //Has the piece been captured
	int colorDisplace; //-1 for white, +1 for black, used to determine whether pawn can go up or down
	boolean check; //Activates for king piece, is true if the king is in the attack range of a piece
	Promote promoteWindow;
	
	ArrayList<Integer> legalXValue = new ArrayList<Integer>(); //Holds legal moves of the piece based on its current position and type
	ArrayList<Integer> legalYValue = new ArrayList<Integer>(); 	//Holds Y value of the legal move in the same array index as the x Value
	ArrayList<Integer> threatX = new ArrayList<Integer>();    //Holds the threatened coordinates of the piece to calc check
	ArrayList<Integer> threatY = new ArrayList<Integer>();   //Holds the Y value
	boolean firstMove; //True if piece hasn't moved - Useful for calculating double move for pawn or castling for king
	
	public Piece(int x, int y, String type, Boolean color) {
		xTile=x;
		yTile=y;
		this.type=type;
		this.color=color;
		firstMove=true;
		captured=false;
		if(color) {
			colorDisplace=-1;
		}
		else {
			colorDisplace=1;
		}
		
		
	}
	
	//Calculates the threatened squares at its current location. 
	
	//Determines all theoretically possible moves, then removes illegal ones according to the rules of the game
	public void calcMoves(){ 
		
		
		
		//Clear Previous legal moves, if any
		legalXValue.clear();
		legalYValue.clear();
		
		
		if(turn!=color) { //automatically will not calculate moves if it isnt this pieces turn
			return;
		}
		
		addMoves(); //Will add all possible moves that this piece can do
		
		
		for(int i=legalXValue.size()-1; i>=0; i--) {
			
			//Remove move if goes out of bounds
			if(legalXValue.get(i)>7 || legalXValue.get(i)<0 || legalYValue.get(i)>7 || legalYValue.get(i)<0) {
				
				removeLegalMoves(i);
				
			}
			else {
				//Check to see if another pieces is on a legalValuetile
				for(int j=0; j<Pieces.length; j++) {
					if(Pieces[j].getX() ==legalXValue.get(i) && Pieces[j].getY() == legalYValue.get(i)) {
						int ilx = legalXValue.get(i); //Illegal X and Y value to be removed
						int ily = legalYValue.get(i); 
						int diffX=xTile-ilx; //Determines the difference between the piece and its allied piece that is blocking it. Used to determine Direction and how far it is
						int diffY=yTile-ily;
						
						
						
						//Remove any pieces it may block
						
							
							//If there a piece in the way, then remove all tiles behind it
							
							//Knights and kings are the exception, as knights can jump over pieces, and the cant be "blocked" as there are not tiles past where allied pieces lie, so we only need to remove the tiles where allied pieces are on
							
							
							//Pawns can only be blocked a piece directly in front of it
							if(type.equals("pawn")) {
								
								if(firstMove) {
									
									if(ily==yTile+2*colorDisplace ) {
										removeLegalMoves(i);
										
									}
									else if(ily==yTile+colorDisplace && !(ilx==xTile+1 || ilx==xTile-1)) {
										removeLegalMoves(i);
										removeLegalMoves(i-1);
										i--;
										
									
									
									}
								}
								else if(ily==yTile+colorDisplace && ilx==xTile){
									removeLegalMoves(i);
								}
								
							}
							else if(type.equals("bishop")){
								//4 cases of tile blocked by ally
								//the tile is in one of the 4 diagonal directions
								//+X+Y, +X-Y, -X+Y, -X-Y
								//So take the difference between the two, and then block all tiles that have a greater or smaller x or y value than them
								//Note: We only remove the piece behind the blocking piece, we determine whether to remove the blocking piece itself later, same for rook and queen
								
								
								if(diffX>0) {
									if(diffY>0) { //Approaching (0,0) or -X -Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)<=ilx-1 && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (0,7) or -X +Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)<=ilx-1 && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								else if(diffX<0) {
									if(diffY>0) { //Approaching (7,0) or +X -Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)>=ilx+1 && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (7,7) or +X +Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)>=ilx+1 && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								
							}
							else if(type.equals("rook")) {
								// 4 cases of tile blocked by ally
								// The tile is blocked either vertically or horizontally
								// Either illegal X or illegal Y has to equal the rooks X Y respectively, as well as the opposite axis being greater than or less than the rooks respective axis
								// X-Y, X+Y, -XY, +XY
								
								if(diffX==0) {
									if(diffY>0) { //Piece is above the rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)==ilx && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffY<0) { //Below rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)==ilx && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								else if(diffY==0) {
									if(diffX>0) { //Piece is to the right of the rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalYValue.get(k)==ily && legalXValue.get(k)<=ilx-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffX<0) {//Piece is to the left of the rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalYValue.get(k)==ily && legalXValue.get(k)>=ilx+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
							}
							
							else if(type.equals("queen")) {
								//The queen simply combines both the rooks and the bishops removals
								
								
								if(diffX>0) {
									if(diffY>0) { //Approaching (0,0) or -X -Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)<=ilx && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (0,7) or -X +Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)<=ilx && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								else if(diffX<0) {
									if(diffY>0) { //Approaching (7,0) or +X -Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)>=ilx && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (7,7) or +X +Y
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)>=ilx && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								
								if(diffX==0) {
									if(diffY>0) { //Piece is above the rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)==ilx && legalYValue.get(k)<=ily-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffY<0) {
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalXValue.get(k)==ilx && legalYValue.get(k)>=ily+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								else if(diffY==0) {
									if(diffX>0) { //Piece is above the rook
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalYValue.get(k)==ily && legalXValue.get(k)<=ilx-1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffX<0) {
										for(int k=legalXValue.size()-1;k>=0; k--) {
											if(legalYValue.get(k)==ily && legalXValue.get(k)>=ilx+1) {
												
												removeLegalMoves(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								
							}
							
							//Now remove the blocking piece if its the same color
							if(color==Pieces[j].getColor()) {
								if(type.equals("knight")){
									
									removeLegalMoves(i);
								
								}
								else if(type.equals("king")) {
								
									removeLegalMoves(i);
								}
								else if(type.equals("bishop")) {
									removeLegalMoves(i);
								}
								else if(type.equals("rook")) {
									removeLegalMoves(i);
								}
								else if(type.equals("queen")) {
									removeLegalMoves(i);
									
								}
								
								
							}
							
						
							
						break;
					} //Removes all pieces behind that blocked tile, if applicable. If the piece on the blocked tile is the saem color, remove that as well
						
					
					
				}
				
			}
			
			
		}
		threatX.clear();
		threatY.clear();
		for(int i=threatX.size()-1; i>=0; i--) {
			
			//Remove move if goes out of bounds
			if(threatX.get(i)>7 || threatX.get(i)<0 || threatY.get(i)>7 || threatY.get(i)<0) {
				
				removeThreat(i);
				
			}
			else {
				//Check to see if another pieces is on a legalValuetile
				for(int j=0; j<Pieces.length; j++) {
					if(Pieces[j].getX() ==threatX.get(i) && Pieces[j].getY() == threatY.get(i)) {
						int ilx = threatX.get(i); //Illegal X and Y value to be removed
						int ily = threatY.get(i); 
						int diffX=xTile-ilx; //Determines the difference between the piece and its allied piece that is blocking it. Used to determine Direction and how far it is
						int diffY=yTile-ily;
						
						
						
						//Remove any pieces it may block
						
							
							//If there a piece in the way, then remove all tiles behind it
							
							//Knights and kings are the exception, as knights can jump over pieces, and the cant be "blocked" as there are not tiles past where allied pieces lie, so we only need to remove the tiles where allied pieces are on
							
							
							//Pawns can only be blocked a piece directly in front of it
							if(type.equals("pawn")) {
								
								if(firstMove) {
									
									if(ily==yTile+2*colorDisplace ) {
										removeThreat(i);
										
									}
									else if(ily==yTile+colorDisplace && !(ilx==xTile+1 || ilx==xTile-1)) {
										removeThreat(i);
										removeThreat(i-1);
										i--;
										
									
									
									}
								}
								else if(ily==yTile+colorDisplace && ilx==xTile){
									removeThreat(i);
								}
								
							}
							else if(type.equals("bishop")){
								//4 cases of tile blocked by ally
								//the tile is in one of the 4 diagonal directions
								//+X+Y, +X-Y, -X+Y, -X-Y
								//So take the difference between the two, and then block all tiles that have a greater or smaller x or y value than them
								//Note: We only remove the piece behind the blocking piece, we determine whether to remove the blocking piece itself later, same for rook and queen
								
								
								if(diffX>0) {
									if(diffY>0) { //Approaching (0,0) or -X -Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)<=ilx-1 && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (0,7) or -X +Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)<=ilx-1 && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								else if(diffX<0) {
									if(diffY>0) { //Approaching (7,0) or +X -Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)>=ilx+1 && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (7,7) or +X +Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)>=ilx+1 && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								
							}
							else if(type.equals("rook")) {
								// 4 cases of tile blocked by ally
								// The tile is blocked either vertically or horizontally
								// Either illegal X or illegal Y has to equal the rooks X Y respectively, as well as the opposite axis being greater than or less than the rooks respective axis
								// X-Y, X+Y, -XY, +XY
								
								if(diffX==0) {
									if(diffY>0) { //Piece is above the rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)==ilx && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffY<0) { //Below rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)==ilx && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								else if(diffY==0) {
									if(diffX>0) { //Piece is to the right of the rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatY.get(k)==ily && threatX.get(k)<=ilx-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffX<0) {//Piece is to the left of the rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatY.get(k)==ily && threatX.get(k)>=ilx+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
							}
							
							else if(type.equals("queen")) {
								//The queen simply combines both the rooks and the bishops removals
								
								
								if(diffX>0) {
									if(diffY>0) { //Approaching (0,0) or -X -Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)<=ilx && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (0,7) or -X +Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)<=ilx && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								else if(diffX<0) {
									if(diffY>0) { //Approaching (7,0) or +X -Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)>=ilx && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
									else if(diffY<0) {//Approaching (7,7) or +X +Y
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)>=ilx && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
											
										}
									}
								}
								
								if(diffX==0) {
									if(diffY>0) { //Piece is above the rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)==ilx && threatY.get(k)<=ily-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffY<0) {
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatX.get(k)==ilx && threatY.get(k)>=ily+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								else if(diffY==0) {
									if(diffX>0) { //Piece is above the rook
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatY.get(k)==ily && threatX.get(k)<=ilx-1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
									else if(diffX<0) {
										for(int k=threatX.size()-1;k>=0; k--) {
											if(threatY.get(k)==ily && threatX.get(k)>=ilx+1) {
												
												removeThreat(k); //Remove all tiles with a smaller x and y value than the piece that blocks it
												
											}
										}
									}
								}
								
							}
							
							//Now remove the blocking piece if its the same color
							if(color==Pieces[j].getColor()) {
								if(type.equals("knight")){
									
									removeThreat(i);
								
								}
								else if(type.equals("king")) {
								
									removeThreat(i);
								}
								else if(type.equals("bishop")) {
									removeThreat(i);
								}
								else if(type.equals("rook")) {
									removeThreat(i);
								}
								else if(type.equals("queen")) {
									removeThreat(i);
									
								}
								
								
							}
							
						
							
						break;
					} //Removes all pieces behind that blocked tile, if applicable. If the piece on the blocked tile is the saem color, remove that as well
						
					
					
				}
				
			}
			
			
		}
		//Remove moves if threatened and the piece is a king
		/*if(type.equals("king")) {
			for (int i=Pieces.length; i>=0; i--) {
				System.out.println("cock1");
			//	System.out.println(Pieces[i].getThreatX().length);
				for(int j=Pieces[i].getThreatX().length; j>=0; j--) {
					System.out.println("cock2");
					for(int k=legalXValue.size()-1; k>=0; k--) {
						System.out.println("cock3");
						if(legalXValue.get(k) == Pieces[i].getThreatX()[j] && legalYValue.get(k) == Pieces[i].getThreatY()[j] && Pieces[i].getColor()!=color) {
							System.out.println("coc4");
							removeLegalMoves(i);
							
							
						}
					}
				}
			}
		}*/
		
		//If no legal moves, setisMoveable to false
		if(legalXValue.size()==0) {
			isMoveable=false;
		}
		else {
			isMoveable=true;
		}
		
		//Add the Tile its currently at for calculation purposes
		legalXValue.add(xTile);
		legalYValue.add(yTile);
		
	
	}

	
	public void setX(int x) {
		xTile=x;
		
		calcMoves();
	}
	public void removeLegalMoves(int index) { //Will remove move from legal moves list at index 
		legalXValue.remove(index);
		legalYValue.remove(index);
		
	}
	public void removeThreat(int index) {
		threatX.remove(index);
		threatY.remove(index);
	}
	public void addLegalMoves(int index) {
		legalXValue.add(index);
		legalYValue.add(index);
	}
	public void setPieces(Piece[] p) {
		Pieces=p;
	}
	public void setY(int y) {
		yTile=y;
		calcMoves();
	}
	
	public void movePiece(int x, int y) {
		xTile=x;
		yTile=y;
		
	}
	public void setFirstMove(boolean b) {
		firstMove=b;
	}
	
	public int getX() {
		return xTile;
	}
	
	public int getY() {
		return yTile;
	}
	public String getType() {
		return type;
	}
	public boolean getIsMoveable() {
		return isMoveable;
	}
	public Boolean getColor() {
		return color;
	}
	public int[] getThreatX() {
		int[] output=new int[threatX.size()];
		for(int i=0; i<threatX.size();i++) {
			output[i]=threatX.get(i);
		}
		return output;
	}
	public int[] getThreatY() {
		int[] output=new int[threatY.size()];
		for(int i=0; i<threatY.size();i++) {
			output[i]=threatY.get(i);
		}
		return output;
	}
	
	public void printPiece() { //Displays all Information on this piece
		System.out.println("       Type: " + type);
		if(color) {
			System.out.println("      Color: White");
		}
		else {
			System.out.println("      Color: Black");
		}
		System.out.println("         X = " + xTile);
		System.out.println("         Y = " + yTile);
		
		System.out.print("Legal Moves:");
		for(int i=0; i<legalXValue.size(); i++) {
			System.out.print(" (" + legalXValue.get(i) + "," + legalYValue.get(i)+ ")");
		}
		System.out.print("\nThreatened:");
		for(int i=0; i<threatX.size(); i++) {
			System.out.print(" (" + threatX.get(i) + "," + threatY.get(i)+ ")");
		}
		System.out.print("\n");
	}
	
	public int[] getLegalX(){ //Converts arraylist and outputs the int[] version
		int[] output=new int[legalXValue.size()];
		for(int i=0; i<legalXValue.size();i++) {
			output[i]=legalXValue.get(i);
		}
		return output;
	}
	public int[] getLegalY(){
		int[] output=new int[legalYValue.size()];
		for(int i=0; i<legalYValue.size();i++) {
			output[i]=legalYValue.get(i);
		}
		return output;
	}
	
	public void setTurn(boolean b) {
		turn=b;
	}
	public void setCaptured(boolean b) {
		captured=b;
		xTile=100;
		yTile=100;
	}
	public void promotePawn() { //Converts this piece into any other valid pawn promotion
		promoteWindow = new Promote();
		String x="";
		while(x.equals("")) {
			//Run until player promotes his pawn
			x=promoteWindow.getType();
			
			System.out.print(""); //I dont know why but it needs this line to run so dont touch it
			
		}
		System.out.print(""); //This too
		type=promoteWindow.getType();
		
	}
	public void addMoves() {
		
				
				
				//Pawn
				//Can Move 1 Tile forward
				//Can Move 2 Tiles forward if its first turn
				//Can Move Diagonally 1 square if there is an enemy piece there
				//En passant (Not implemented)
				
				
				if(type.equals("pawn")) {
					
					
					
					//Adds the double forward move if the pawn has not moved yet
					if(firstMove) {
							legalXValue.add(xTile);
							legalYValue.add(yTile+colorDisplace*2);
							
					}
					//Adds the diagonal attack if there is an opponent piece there
					for(int i=0; i<Pieces.length; i++) {
						
						if(Pieces[i].getX()==xTile+1 && Pieces[i].getY()==yTile+colorDisplace && Pieces[i].getColor()!=color) {
							legalXValue.add(xTile+1);
							legalYValue.add(yTile+colorDisplace);
							threatX.add(xTile+1);
							threatY.add(yTile+colorDisplace);
						}
						if(Pieces[i].getX()==xTile-1 && Pieces[i].getY()==yTile+colorDisplace && Pieces[i].getColor()!=color) {
							legalXValue.add(xTile-1);
							legalYValue.add(yTile+colorDisplace);
							threatX.add(xTile-1);
							threatY.add(yTile+colorDisplace);
						}
					}
					
					//Adds normal forward movement
					legalXValue.add(xTile);
					legalYValue.add(yTile+colorDisplace);
				}
				
				
				//Bishop can move diagonally in any direction
				
				if(type.equals("bishop")) {
					for(int i=1; i<8; i++) {
						
						//+x +y
						legalXValue.add(xTile+i);
						legalYValue.add(yTile+i);
						threatX.add(xTile+i);
						threatY.add(yTile+i);
						
						//-x +y
						legalXValue.add(xTile-i);
						legalYValue.add(yTile+i);
						threatX.add(xTile-1);
						threatY.add(yTile+i);
						
						//+x -y
						legalXValue.add(xTile+i);
						legalYValue.add(yTile-i);
						threatX.add(xTile+i);
						threatY.add(yTile-1);
						
						//-x -y
						legalXValue.add(xTile-i);
						legalYValue.add(yTile-i);
						threatX.add(xTile-1);
						threatY.add(yTile-1);
					}
					
				}
				
				//Knights can move 2 tiles out in one direction and 1 tile perpendicular to that direction
				
				if(type.equals("knight")) {
					//+2x +y
					legalXValue.add(xTile+2);
					legalYValue.add(yTile+1);
					threatX.add(xTile+2);
					threatY.add(yTile+1);
					
					//-2x +y
					legalXValue.add(xTile-2);
					legalYValue.add(yTile+1);
					threatX.add(xTile-2);
					threatY.add(yTile+1);
					
					//+2x -y
					legalXValue.add(xTile+2);
					legalYValue.add(yTile-1);
					threatX.add(xTile+2);
					threatY.add(yTile-1);
					
					//-2x -y
					legalXValue.add(xTile-2);
					legalYValue.add(yTile-1);
					threatX.add(xTile-2);
					threatY.add(yTile-1);
					
					//+x +2y
					legalXValue.add(xTile+1);
					legalYValue.add(yTile+2);
					threatX.add(xTile+1);
					threatY.add(yTile+2);
					
					//-x +2y
					legalXValue.add(xTile-1);
					legalYValue.add(yTile+2);
					threatX.add(xTile-1);
					threatY.add(yTile+2);

					//+x -2y
					legalXValue.add(xTile+1);
					legalYValue.add(yTile-2);
					threatX.add(xTile+1);
					threatY.add(yTile-2);
					
					//-x -2y
					legalXValue.add(xTile-1);
					legalYValue.add(yTile-2);
					threatX.add(xTile-1);
					threatY.add(yTile-2);
				}
				
				//Rooks can move in a straight line vertically or horizontally
				
				
				if(type.equals("rook")) {
						for(int i=1; i<8; i++) {
						
						//+x 
						legalXValue.add(xTile+i);
						legalYValue.add(yTile);
						threatX.add(xTile+1);
						threatY.add(yTile);
						
						//-x 
						legalXValue.add(xTile-i);
						legalYValue.add(yTile);
						threatX.add(xTile-i);
						threatY.add(yTile);
						
						//-y
						legalXValue.add(xTile);
						legalYValue.add(yTile-i);
						threatX.add(xTile);
						threatY.add(yTile-i);
						
						//+y
						legalXValue.add(xTile);
						legalYValue.add(yTile+i);
						threatX.add(xTile);
						threatY.add(yTile+i);
						
						
					}
				}
				//Queens can move the same as a rook and a bishop
				
				if(type.equals("queen")) {
					for(int i=1; i<8; i++) {
						
						//+x 
						legalXValue.add(xTile+i);
						legalYValue.add(yTile);
						threatX.add(xTile+i);
						threatY.add(yTile);
						//-x 
						legalXValue.add(xTile-i);
						legalYValue.add(yTile);
						threatX.add(xTile-i);
						threatY.add(yTile);
						
						//-y
						legalXValue.add(xTile);
						legalYValue.add(yTile-i);
						threatX.add(xTile);
						threatY.add(yTile-i);
						
						//+y
						legalXValue.add(xTile);
						legalYValue.add(yTile+i);
						threatX.add(xTile);
						threatY.add(yTile+i);
						
						//+x +y
						legalXValue.add(xTile+i);
						legalYValue.add(yTile+i);
						threatX.add(xTile+i);
						threatY.add(yTile+i);
						
						//-x +y
						legalXValue.add(xTile-i);
						legalYValue.add(yTile+i);
						threatX.add(xTile-i);
						threatY.add(yTile+i);
						
						//+x -y
						legalXValue.add(xTile+i);
						legalYValue.add(yTile-i);
						threatX.add(xTile+i);
						threatY.add(yTile-i);
						
						//-x -y
						legalXValue.add(xTile-i);
						legalYValue.add(yTile-i);
						threatX.add(xTile-i);
						threatY.add(yTile-i);
						
					}
				}
				
				//King can move one tile in every direction, unless tile is threatened by enemy piece.
				//King can also "castle", where if the rook and the king are in direct sight and have not moved, can have the king move two spaces towards the rook, and the rook on the other side of the king
				//Ex: S is for empty square, K is for king, R is for rook
				// SSSKSSR --> SSSSRKS 
				if(type.equals("king")) {
					if(firstMove) {
						for(int i=0; i<Pieces.length; i++) {
							if(Pieces[i].getColor()==color && Pieces[i].getType().equals("rook") && Pieces[i].getX()== xTile+3 && Pieces[i].getY()==yTile) {
								legalXValue.add(xTile+2);
								legalYValue.add(yTile);
							}
							else if(Pieces[i].getColor()==color && Pieces[i].getType().equals("rook") && Pieces[i].getX()== xTile-4 && Pieces[i].getY()==yTile) {
								legalXValue.add(xTile-2);
								legalYValue.add(yTile);
							}
						}
					}
					//+x 
					legalXValue.add(xTile+1);
					legalYValue.add(yTile);
					threatX.add(xTile+1);
					threatY.add(yTile);
					
					//-x 
					legalXValue.add(xTile-1);
					legalYValue.add(yTile);
					threatX.add(xTile-1);
					threatY.add(yTile);
					
					//-y
					legalXValue.add(xTile);
					legalYValue.add(yTile-1);
					threatX.add(xTile);
					threatY.add(yTile-1);
					
					//+y
					legalXValue.add(xTile);
					legalYValue.add(yTile+1);
					threatX.add(xTile);
					threatY.add(yTile+1);
					
					//+x +y
					legalXValue.add(xTile+1);
					legalYValue.add(yTile+1);
					threatX.add(xTile+1);
					threatY.add(yTile+1);
					
					//-x +y
					legalXValue.add(xTile-1);
					legalYValue.add(yTile+1);
					threatX.add(xTile-1);
					threatY.add(yTile+1);
					
					//+x -y
					legalXValue.add(xTile+1);
					legalYValue.add(yTile-1);
					threatX.add(xTile+1);
					threatY.add(yTile-1);
					
					//-x -y
					legalXValue.add(xTile-1);
					legalYValue.add(yTile-1);
					threatX.add(xTile-1);
					threatY.add(yTile-1);
				}
	}
	
	
		
}
