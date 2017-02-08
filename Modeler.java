/* A program to draw a stellated tetrahedron with different colored triangles
 * and rotate the scene
 */
import java.awt.BorderLayout;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.glu.GLU;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.*;

import com.jogamp.opengl.awt.GLJPanel;

public class Modeler extends GLJPanel implements GLEventListener,ActionListener,MouseMotionListener, MouseListener
{
	
	int moving,startx,starty;
	float angx=0,angy,angz;
	private GLU glu;
	//variables for moving cube
	
	float x,y;
	static JButton okButton;
	//
	public static void main(String args[])
	{
		JFrame window = new JFrame("Modeling Software");
		 okButton = new JButton("Translate");
        // The canvas
        Modeler panel = new Modeler();
        panel.setPreferredSize(new Dimension(1000,1000));       
        JPanel content=new JPanel();
        
        content.setLayout(new BorderLayout());
    	content.add(panel, BorderLayout.CENTER);
    	content.add(okButton, BorderLayout.WEST);
        okButton.addActionListener(panel);
        
        window.setContentPane(content);
        window.pack();
        window.setLocation(0,0);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        panel.requestFocusInWindow();		
	}
    Modeler()
    {
    	super( new GLCapabilities(null) ); // Makes a panel with default OpenGL "capabilities".
    	GLJPanel drawable = new GLJPanel();               // new GLJPanel inside GLJPanel
    	drawable.setPreferredSize(new Dimension(600,600));
    	setLayout(new BorderLayout());
    	add(drawable, BorderLayout.CENTER);
    	drawable.addGLEventListener(this); // Set up events for OpenGL drawing!
    	drawable.addMouseListener(this);
    	drawable.addMouseMotionListener(this);
    	
    }
    public void actionPerformed(ActionEvent e) {
//		System.exit(0);
	x+=0.02f;
	repaint();
	}
  void drawMesh(GL2 gl)
  {
	  
	  for(int i=0;i<40;i++)
		  for(int j=0;j<40;j++)
			  {
			  gl.glBegin(GL2.GL_LINE_LOOP);
			  gl.glVertex3f((float)i/20,(float)j/20,0.0f);
			  gl.glVertex3f((float)(i+1)/20,(float)j/20,0.0f);
			  gl.glVertex3f((float)(i+1)/20,(float)(j+1)/20,0.0f);
			  gl.glVertex3f((float)(i)/20,(float)(j+1)/20,0.0f);
			  gl.glEnd();
			  }
	  
  }
  private void square(GL2 gl2, double r, double g, double b) {
      gl2.glColor3d(r,g,b);
      gl2.glBegin(GL2.GL_POLYGON);
      gl2.glNormal3f(0,0,1);
     
      gl2.glVertex3d(-0.5, -0.5, 0.5);
      
      gl2.glVertex3d(0.5, -0.5, 0.5);
    
      gl2.glVertex3d(0.5, 0.5, 0.5);
    
      gl2.glVertex3d(-0.5, 0.5, 0.5);
      gl2.glEnd();
  }
  
  private void cube(GL2 gl2, double size) {
      gl2.glPushMatrix();
      gl2.glScaled(size,size,size); // scale unit cube to desired size
      
      square(gl2,1, 0, 0); // red front face
      
      gl2.glPushMatrix();
      gl2.glRotated(90, 0, 1, 0);
      square(gl2,1, 1, 1); // green right face
      gl2.glPopMatrix();
      
      gl2.glPushMatrix();
      gl2.glRotated(-90, 1, 0, 0);
      square(gl2,0, 1, 0); // blue top face
      gl2.glPopMatrix();
      
      gl2.glPushMatrix();
      gl2.glRotated(180, 0, 1, 0);
      square(gl2,1, 1, 1); // cyan back face
      gl2.glPopMatrix();
      
      gl2.glPushMatrix();
      gl2.glRotated(-90, 0, 1, 0);
      square(gl2,1, 1, 1); // magenta left face
      gl2.glPopMatrix();
      
      gl2.glPushMatrix();
      gl2.glRotated(90, 1, 0, 0);
      square(gl2,1, 1, 1); // yellow bottom face
      gl2.glPopMatrix();
      
      gl2.glPopMatrix(); // Restore matrix to its state before cube() was called.
  }
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl=arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glRotatef(angx,1, 0, 0); 
		gl.glRotatef(angy,0, 1, 0);
		gl.glRotatef(angz,0, 0, 1);
		glu.gluLookAt(1.0f, 1.0f, 0, 1, 1, 1, 0, 1, 0);
		drawMesh(gl);
		gl.glTranslatef(x+1.0f,1.0f,0);
		cube(gl,(1.0f/10));
		gl.glEnd();
		gl.glFlush();
		repaint();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl=arg0.getGL().getGL2();
		gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
		
		moving=0;
		gl.glEnable(GL2.GL_DEPTH_TEST);
		 glu = new GLU();
		 x=0;y=0;
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int x, int y, int w,
			int h) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		 
	    gl.glViewport(0, 0, w, h);
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    if (w <= h) //
	    gl.glOrtho(-2.0, 2.0, -2.0 * (float) h / (float) w, //
	        2.0 * (float) h / (float) w, -20.0, 20.0);
	    else gl.glOrtho(-2.0 * (float) w / (float) h, //
	        2.0 * (float) w / (float) h,//
	        -2.0, 2.0, -20.0, 20.0);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
			
		
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
		// TODO Auto-generated method stub
		moving=1;
		startx=arg0.getX();
		starty=arg0.getY();
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
			moving=0;
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
int x,y;
		
		x=arg0.getX();
		y=arg0.getY();
			angy=angy+(x-startx);
			angz=angz+(y-starty);
			startx=x;
			starty=y;
			repaint();
	
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
