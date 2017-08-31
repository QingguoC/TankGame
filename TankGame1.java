package no11;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
/**
 * @author GuoFang
 *
 */
public class TankGame1 extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       TankGame1 tk = new TankGame1();
      
	}
    public TankGame1 () {
    	 myPanel mp = new myPanel();
    	 Thread t2 = new Thread(mp);
    	 t2.start();
         this.add(mp);
         this.addKeyListener(mp);
    	 setSize(600, 400);
    	 setResizable(false);
    	 this.setDefaultCloseOperation(EXIT_ON_CLOSE);;
         setVisible(true);
    }
}
class Enemy extends Tank implements Runnable {

	public Enemy(int x, int y, int life) {
		super(x, y,life);
		
	}
	
    private double turn, shot; 
	public void run() {
		while (true) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			turn = Math.random();
			shot = Math.random();
			if(turn > 0.1) {
				this.goStraight();
			} else this.turn();
			if(shot<0.05) {
				this.shot();
			}
				
			if(!this.isAlive()) break;
			
		}
		
	}
	
}
class myPanel extends JPanel implements KeyListener, Runnable{


	Tank hero;
	Vector<Enemy> ens;
	private int numEns;
	private Enemy temp;
	Image im1, im2, im3;
    public myPanel() {
    	hero = new Tank(270, 340,1);
    	hero.setDirection(0);
    	ens = new Vector<Enemy> ();
    	try {
			im1 = ImageIO.read(new File("1.gif"));
			im2 = ImageIO.read(new File("2.gif"));
			im3 = ImageIO.read(new File("3.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//im1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/1.gif"));
    	//im2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.gif"));
    	//im3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.gif"));
    	numEns = 3;
    	for(int i=0; i<numEns; i++) {
    		temp = new Enemy(i*100+20, 0,1);
    		ens.add(temp);
    		Thread t2 = new Thread(temp);
    		t2.start();
    		
    	}
    }
	public void paint (Graphics g) {
		super.paint(g);
		g.fill3DRect(0, 0, this.getWidth(), this.getHeight(), true);
		if(hero!= null&&hero.isAlive()) { drawTank(hero.getX(), hero.getY(), g, 0,hero.getDirection());
		if(hero.isShot()) {
			for(Bullet b : hero.bv) {
				if(b.isAlive()) 
				g.fillOval(b.getX(), b.getY(), 3, 3);
				else hero.removeEShot();
			}
		}
		} else {
			
			 hero = null;
			JOptionPane.showMessageDialog(null, 
					"你结束了!!!", 
					"Game Over !", JOptionPane.ERROR_MESSAGE);
		
		System.exit(0) ; 
} 
		for(int i=0; i<ens.size(); i++) {
			temp = ens.get(i);
        if(temp.isAlive()) {
        	drawTank(temp.getX(),temp.getY(),g,1,temp.getDirection());
        
        } else  {
        	
        	this.ens.remove(temp);
        }
        if(temp.isShot()) {
			for(Bullet b : temp.bv) {
				if(b.isAlive()) 
				g.fillOval(b.getX(), b.getY(), 3, 3);
				else temp.removeEShot();
			}
		}
		}
		for(Bomb b : bombs) {
			if(b.isAlive()) {
				
				if(b.life>6) {
					g.drawImage(im1, b.x, b.y, 10, 10, this);
				} else if(b.life >3) {
					g.drawImage(im2, b.x, b.y, 20, 20, this);
				} else g.drawImage(im3, b.x, b.y, 30, 30, this);
				b.downLife();
			}
			else bombs.remove(b);
		}
       
	}
    public void HitTank(Bullet b, Tank e) {
    	if(b.getX()>e.getX()&&b.getY()>e.getY()) {
    		switch (e.getDirection()) {
        	case 0:
        	case 1:
        		if(b.getX()<(e.getX()+20)&&b.getY()<(e.getY()+25)) {
        			b.setAlive(false);
        			e.setLife(e.getLife()-1);
        			if(e.getLife() == 0) {
        				Bomb bomb = new Bomb(e.getX(), e.getY());
        				bombs.add(bomb);
        			}
        		} break;
        	case 2:
        	case 3:
        		if(b.getX()<(e.getX()+25)&&b.getY()<(e.getY()+20)) {
        			b.setAlive(false);
        			e.setLife(e.getLife()-1);
        			if(!e.isAlive()) {
        				Bomb bomb = new Bomb(e.getX(), e.getY());
        				bombs.add(bomb);
        			}
        		} break;
        	}
    	}
    	
    }
    CopyOnWriteArrayList<Bomb> bombs = new CopyOnWriteArrayList<Bomb>();
	public void drawTank(int x, int y, Graphics g, int type, int direction) {
		switch (type) {
		case 0: g.setColor(Color.CYAN); break;
		case 1: g.setColor(Color.BLUE); break;
		}
		switch (direction) {
		case 0: {
			g.fill3DRect(x, y, 5, 25, true);
			g.fill3DRect(x+15, y, 5, 25, true);
			g.fill3DRect(x+5, y+5, 10, 15, true);
			g.setColor(Color.RED);
			g.fillOval(x+4, y+7, 10, 10);
			g.drawLine(x+9, y-2, x+9, y+10);
		} break;
		case 1: {
			g.fill3DRect(x, y, 5, 25, true);
			g.fill3DRect(x+15, y, 5, 25, true);
			g.fill3DRect(x+5, y+5, 10, 15, true);
			g.setColor(Color.RED);
			g.fillOval(x+4, y+7, 10, 10);
			g.drawLine(x+9, y+15, x+9, y+27);
		} break;
        case 2: {
        	g.fill3DRect(x, y, 25, 5, true);
			g.fill3DRect(x, y+15, 25, 5, true);
			g.fill3DRect(x+5, y+5, 15, 10, true);
			g.setColor(Color.RED);
			g.fillOval(x+7, y+4, 10, 10);
			g.drawLine(x-2, y+9, x+10, y+9);
		} break;
        case 3: {
        	g.fill3DRect(x, y, 25, 5, true);
			g.fill3DRect(x, y+15, 25, 5, true);
			g.fill3DRect(x+5, y+5, 15, 10, true);
			g.setColor(Color.RED);
			g.fillOval(x+7, y+4, 10, 10);
			g.drawLine(x+15, y+9, x+27, y+9);
		} break;
		
		}
	}
	public void keyTyped(KeyEvent e) {
		
		
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			hero.setDirection(1);
//			for(Enemy en : ens) {
//		          if(hero!=null&&en.KeepDistance(hero)) {
//		        	  en.setKeepDistance(true);
//		        	  hero.setKeepDistance(true);
//		          }
//						else {en.setKeepDistance(false);
//						      en.turn();
//						      hero.setKeepDistance(false);}
//					}
			hero.goStraight();
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP) {
			hero.setDirection(0);
//			for(Enemy en : ens) {
//		          if(hero!=null&&en.KeepDistance(hero)) {
//		        	  en.setKeepDistance(true);
//		        	  hero.setKeepDistance(true);
//		          }
//						else {en.setKeepDistance(false);
//						      en.turn();
//						      hero.setKeepDistance(false);}
//					}
			hero.goStraight();
			
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			hero.setDirection(2);
//			for(Enemy en : ens) {
//		          if(hero!=null&&en.KeepDistance(hero)) {
//		        	  en.setKeepDistance(true);
//		        	  hero.setKeepDistance(true);
//		          }
//						else {en.setKeepDistance(false);
//						      en.turn();
//						      hero.setKeepDistance(false);}
//					}
			hero.goStraight();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			
			hero.setDirection(3);
//			for(Enemy en : ens) {
//		          if(hero!=null&&en.KeepDistance(hero)) {
//		        	  en.setKeepDistance(true);
//		        	  hero.setKeepDistance(true);
//		          }
//						else {en.setKeepDistance(false);
//						      en.turn();
//						      hero.setKeepDistance(false);}
//					}
			
			hero.goStraight();
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
		  if(this.hero.bv.size()<5) {
			  hero.shot();
		  }
		}
		//this.repaint();
		
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void run() {
		while(hero != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Enemy en : ens) {
		          if(hero!=null&&en.KeepDistance(hero)) {
		        	  en.setKeepDistance(true);
		        	  hero.setKeepDistance(true);
		          }
						else {en.setKeepDistance(false);
						      en.turn();
						      hero.setKeepDistance(false);}
					}
			for(int i=0; i< ens.size();i++){
				for(int j=i+1; j<ens.size();j++) {
					if(ens.get(i).KeepDistance(ens.get(j))) {
						ens.get(i).setKeepDistance(true);
						ens.get(j).setKeepDistance(true);
					}
					else{
						ens.get(i).setKeepDistance(false);
						ens.get(j).setKeepDistance(false);
						ens.get(i).turn();
						ens.get(j).turn();
					}
				}
				if(hero!=null) {
					for(Bullet b : hero.bv) {
						this.HitTank(b, ens.get(i));
					}
				}
				if(hero!=null) {
					for(Enemy en : ens) {
						for(Bullet b : en.bv) {
							this.HitTank(b, hero);
						}
					}
				}
			}
			this.repaint();
		}
		
	}
}
class Tank {
	private int x=0,y=0;
	private int speed = 5;
	private int direction = 1;
	private int life;
	public int getLife() {
		return life;
	}
	public Rect nextRect() {
		switch (direction) {
		case 0: return new Rect(x,x+20,y-speed, y+25-speed);
		case 1: return new Rect(x,x+20,y+speed, y+25+speed);
		case 2: return new Rect(x-speed,x+20-speed,y, y+25);
		case 3: return new Rect(x+speed, x+20+speed, y, y+25);
		}
		return null;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public boolean isAlive() {
		return life > 0;
	}
	public int getDirection() {
		
		return direction;
	}
	CopyOnWriteArrayList <Bullet> bv = new CopyOnWriteArrayList<Bullet>();
	public void shot() {
		if(life>0) {
			switch (direction) {
			case 0: bv.add(new Bullet(x+9, y-2, 0)); break;
			case 1: bv.add(new Bullet(x+9, y+27, 1)); break;
			case 2: bv.add(new Bullet(x-2, y+9, 2)); break;
			case 3: bv.add(new Bullet(x+27, y+9, 3)); break;
			}
		}
		
		for(Bullet b : bv) {
			Thread t = new Thread(b);
			t.start();
		}
	}
	public boolean KeepDistance(Tank tank) {
		return !(this.nextRect().intersects(tank.nextRect()));
		
	}

	public void removeEShot() {
		for(Bullet b : bv) {
			if(!b.isAlive()) bv.remove(b);

		}
       
	}
	public boolean isShot() {
		return bv!=null;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
    public boolean isKeepDistance() {
		return keepDistance;
	}
	public void setKeepDistance(boolean keepDistance) {
		this.keepDistance = keepDistance;
	}
	private boolean keepDistance = true;
	public void goStraight() {
		if(keepDistance) {
			switch(this.getDirection()) {
			case 0: this.moveUp(); break;
			case 1: this.moveDown(); break;
			case 2: this.moveLeft(); break;
			case 3: this.moveRight(); break;
			}
		}
	}
	public void turn() {
		double turnN = Math.random();
//		switch(this.getDirection()) {
//		case 0: if(turnN > 0.66) this.moveRight();
//		else if(turnN > 0.33) this.moveDown();
//		else this.moveLeft();break;
//		case 1: if(turnN > 0.66) this.moveRight();
//		else if(turnN > 0.33) this.moveUp();
//		else this.moveLeft();break;
//		case 2: if(turnN > 0.66) this.moveRight();
//		else if(turnN > 0.33) this.moveDown();
//		else this.moveUp();break;
//		case 3: if(turnN > 0.66) this.moveDown();
//		else if(turnN > 0.33) this.moveUp();
//		else this.moveLeft();break;
//		}
		switch(this.getDirection()) {
		case 0: if(turnN > 0.66) this.setDirection(1);
		else if(turnN > 0.33) this.setDirection(2);
		else this.setDirection(3);break;
		case 1: if(turnN > 0.66) this.setDirection(0);
		else if(turnN > 0.33) this.setDirection(2);
		else this.setDirection(3);break;
		case 2: if(turnN > 0.66) this.setDirection(1);
		else if(turnN > 0.33) this.setDirection(0);
		else this.setDirection(3);break;
		case 3: if(turnN > 0.66) this.setDirection(1);
		else if(turnN > 0.33) this.setDirection(2);
		else this.setDirection(0);break;
		}
	}
	public void moveUp() {
		if(y >= 5 ) {
			y -= speed;
		}
		direction = 0;
	}
	public void moveDown() {
		if(y <= 345 ) {
			y += speed;
		}
		direction = 1;
	}
	public void moveLeft() {
		if(x >= 5) {
			x -= speed;
		}
		direction = 2;
	}
	public void moveRight() {
		if(x <= 570 ) {
			x += speed;
		}
		direction = 3;
	}
	public Tank(int x, int y, int life) {
		this.x = x;
		this.y = y;
		this.life = life;
	}
	
}
class Bomb {
	int x,y;
	int life;
	public Bomb(int x, int y) {
		life = 9;
		this.x = x;
		this.y = y;
	}
	public void downLife() {
		if(life>0){
			life--;
		} else life = 0;
	}
	public boolean isAlive() {
		return life > 0;
	}
}
class Rect {
	private int xmin, xmax, ymin, ymax;
	public double xmin() {
    	return xmin;// minimum x-coordinate of rectangle
    }
    public double ymin() {
    	return ymin;// minimum y-coordinate of rectangle
    }
    public double xmax() {
    	return xmax;// maximum x-coordinate of rectangle
    }
    public double ymax() {
    	return ymax;// maximum y-coordinate of rectangle
    }
	public Rect(int xmin, int xmax, int ymin, int ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	   public boolean intersects(Rect that) {
	    	if(that == null) return false;
		   if(xmax < that.xmin||xmin> that.xmax) return false;
		   if(ymax < that.ymin||ymin> that.ymax) return false;
		   return true;// does this rectangle intersect that rectangle (at one or more points)?
	    }
}
class Bullet implements Runnable {
	private int x;
	private int speed=10;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	private int y;
	private int direction;
	private boolean isAlive=true;
	public Bullet(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	public void run() {
		while(isAlive){
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (direction) {
			case 0: y -= speed; break;
			case 1: y += speed; break;
			case 2: x -= speed; break;
			case 3: x += speed; break;
			}
			if(x<=0||x>=600||y<=0||y>=400) {
				setAlive(false);
			}
		}
		}

}