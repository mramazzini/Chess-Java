import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MListener implements MouseMotionListener, MouseListener{
	
	private int mousex;
	private int mousey;
	private boolean pressed=false;
	private boolean illegalMove=false;
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		
	}
	
	public int getMouseX() {
		return mousex;
	}

	public int getMouseY() {
		return mousey;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
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
	public void mousePressed(MouseEvent e) {
		
		if(!pressed) {
			pressed=true;
			mousex=e.getX();
			mousey=e.getY();
			
		}
		else if(illegalMove){
			pressed=true;
			mousex=e.getX();
			mousey=e.getY();
			
		}
		else {
			pressed=false;
			mousex=e.getX();
			mousey=e.getY();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public boolean getPressed() {
		return pressed;
	}
	public void setPressed(boolean p) {
		pressed=p;
	}
	public void setIllegalMove(boolean b) {
		illegalMove=b;
	}

}
