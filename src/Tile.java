


import java.awt.Toolkit;

import javax.swing.JPanel;

public class Tile {
	
	
	
	private JPanel Tile;
	
	
	private final int TileXsize = 80;
	private final int TileYsize = TileXsize;
	
	
	//Board goes from A8 to H1 with the array being [0][0] to [7][7] respectively
	private int TileX;
	private int TileY;
	
	public Tile(int x,int y) {
		
		
		Tile = new JPanel();
		Tile.setSize(TileXsize,TileYsize);
		TileX=(int) (TileXsize+TileXsize*x);
		TileY=(int) (TileYsize+TileYsize*y);
		Tile.setLocation(TileX,TileY);
		
		
		
	}
	
	
	public int[] getTileXY() {
		int[] Tcoord = new int[2];
		Tcoord[0]=TileX;
		Tcoord[1]=TileY;
		return Tcoord;
	}
	public JPanel getTile() {
		return Tile;
	}
	

}
