package org.yourorghere;



// Import classes used for reading in the byte data for images.
import org.yourorghere.Vertex;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
// Import classes for OpenGL.
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;
/**
 * This class provides handling for OpenGL events in NeHe's Lesson 19.
 * 
 * @author Irene Kam
 *
 */
public class GLRender implements GLEventListener {
   // The parent object which employs this event handler.
   private Snow m_oParent;
   // Create an array of 1 elements holding the texture names.
   //private int[] m_aTextures = new int[1];
   // Array of bytes from the texture image.
   private byte[] m_aImagePixelBytes;
   // Buffered image for the texture.
   private BufferedImage m_oBufferedImage;
   // Number of particles to display.
   private static final int MAX_PARTICLES = 4000;
   // The array of particles.
   private Vertex[] m_aVertexs = new Vertex[MAX_PARTICLES];
   // The animator to drive the display method.
   private Animator m_oAnimator;
   public int snowList;

   /**
    * Constructor for this event handler.
    * 
    * @param parent The parent object which employs this event handler.
    */
   public GLRender(Snow parent) {
      m_oParent = parent;
   }

   /**
    * Called only once, after OpenGL is initialized. Perform one time 
    * initialization tasks here.
    * 
    * @param drawable The object capablable of drawing OpenGL objects.
    */
   public void init(GLAutoDrawable drawable) {
      // Obtain the GL instance so we can perform OpenGL functions.
      GL gl = drawable.getGL();
      snowList(gl);
      // Set up the animator for the drawable.
      m_oAnimator = new Animator(drawable);
      // Enable smooth shading.
      gl.glShadeModel(GL.GL_SMOOTH);
      // Bind texture to 2D.
      //gl.glBindTexture(GL.GL_TEXTURE_2D, m_aTextures[0]);
      // Set the background / clear color.
      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
      // Clear the depth
      gl.glClearDepth(1.0);
      // Disable depth testing.
//      gl.glEnable(GL.GL_DEPTH_TEST);
      // Type of depth testing.
      //gl.glDepthFunc(GL.GL_LEQUAL);
      // Enable blending and specify blening function.
      gl.glEnable(GL.GL_BLEND);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
      // Get nice perspective calculations. 
      gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
      // Nice point smoothing.
      gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);
      // Enable texture mapping.
      gl.glEnable(GL.GL_TEXTURE_2D);

      try{
         background = TextureIO.newTexture(new File("data/FirstSnow.JPG"), false);
         background.enable();
         background.bind();
         texture = TextureIO.newTexture(new File("data/Particle.jpg"), true);
         texture.enable();
         texture.bind();

         
      } catch(Exception e){
         System.err.println("Load texture failed");
         System.exit(1);
      }
      // Set up texture mappings from the image we have loaded.
      //loadGLTextures(drawable);
      // Create and initialize the 1000 particles.
      initVertexs();
      // Reset the OpenGL drawable first.
      resizeGLScene(
         drawable,
         drawable.getWidth(),
         drawable.getHeight());
      // Start the animator so our scene is animated.
      Vertex.MAX_PARTICLES = MAX_PARTICLES;
      for (int i = 0; i < MAX_PARTICLES; i++) {
         ((Vertex)m_aVertexs[i]).setIndex(i);
      }
      int vertexPerFrame = 200;
      for (int i = 0; i < MAX_PARTICLES/vertexPerFrame; i++) {
          for(int j = 0; j < vertexPerFrame; j++){
              ((Vertex)m_aVertexs[vertexPerFrame*i + j]).setLife(1.0f-j);
          }
//         ((Vertex)m_aVertexs[5*i + 1]).setLife(0.0f);
//         ((Vertex)m_aVertexs[5*i + 2]).setLife(-1.0f);
//         ((Vertex)m_aVertexs[5*i + 3]).setLife(-2.0f);
//         ((Vertex)m_aVertexs[5*i + 4]).setLife(-3.0f);
      }
      m_oAnimator.start();
   }

   /**
    * Causes OpenGL rendering to the given GLAutoDrawable. In this application,
    * draws the 1000 particles on the screen.
    * 
    * @param drawable The OpenGL drawable used to display the particles. 
    */
   Texture texture, background;
   public void snowList(GL gl){
      snowList = gl.glGenLists(1);
      gl.glNewList(snowList,gl.GL_COMPILE);  
      // Draw the particle using triangle strips.
      gl.glBegin(GL.GL_QUADS);
       // Map the texture and create the vertices for the particle.
      gl.glTexCoord2d(1, 1);
      gl.glVertex3f(.2f, .2f, 0);
      gl.glTexCoord2d(0, 1);
      gl.glVertex3f(-.2f, .2f, 0);
      gl.glTexCoord2d(0, 0);
      gl.glVertex3f(-.2f, -.2f, 0);
      gl.glTexCoord2d(1, 0);
      gl.glVertex3f(.2f, -.2f, 0);
      gl.glEnd();
      gl.glEndList();
   }
   public void display(GLAutoDrawable drawable) {
      // Obtain the GL instance so we can perform OpenGL functions.
      GL gl = drawable.getGL();
      // Clear the screen and depth buffer.
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
      // Reset the view.
      gl.glLoadIdentity();

      Vertex particle = null;
      
      // Loop through the Vertexs array and draw each particle.
      for (int i = 0; i < MAX_PARTICLES; i++) {
         particle = m_aVertexs[i];
         if (particle.isAlive()) {
            // This particular particle is alive. 
            handleLiveVertex(gl, particle);
         } else {
            // This particular particle is dead.
            handleDeadVertex(gl, particle);
         }
      }
      drawBackground(gl);
   }
   public void drawBackground(GL gl){
      background.bind();
      background.enable();
      gl.glColor3f(1.0f, 1.0f, 1.0f);
      gl.glBegin(GL.GL_QUADS);
      // Map the texture and create the vertices for the particle.
      gl.glTexCoord2d(1, 0);
      gl.glVertex3f(18f, 14f, m_oParent.m_fZoom);
      gl.glTexCoord2d(0, 0);
      gl.glVertex3f(-18f, 14f, m_oParent.m_fZoom);
      gl.glTexCoord2d(0, 1);
      gl.glVertex3f(-18f, -14f, m_oParent.m_fZoom);
      gl.glTexCoord2d(1, 1);
      gl.glVertex3f(18f, -14f, m_oParent.m_fZoom);
      gl.glEnd();
   }

   public void reshape(
      GLAutoDrawable drawable,
      int x,
      int y,
      int width,
      int height) {

   }

   /**
    * Called when the display device has been changed. Not used in this
    * application.
    */
   public void displayChanged(
      GLAutoDrawable drawable,
      boolean modeChanged,
      boolean deviceChanged) {
      System.out.println("In displayChanged() method.");
   }

   /**
    * Resets the OpenGL's viewport, projection matrix, etc.
    * 
    * @param width Current width of window.
    * @param height Current height of window.
    */
   public GLU glu;
   private void resizeGLScene(
      GLAutoDrawable drawable,
      double width,
      double height) {
      // Obtain the GL and GLU instance so we can perform OpenGL functions.
      GL gl = drawable.getGL();
      glu = new GLU();

      // Make sure height is > 0.
      if (height == 0) {
         height = 1;
      }

      // Reset view port.
      Double widthDouble = new Double(width);
      Double heightDouble = new Double(height);
      gl.glViewport(0, 0, widthDouble.intValue(), heightDouble.intValue());

      // Select and reset the Projection Matrix.
      gl.glMatrixMode(GL.GL_PROJECTION);
      gl.glLoadIdentity();

      // Calculate The Aspect Ratio Of The Window
      glu.gluPerspective(45.0d, width / height, 0.1d, 100.0d);

      gl.glMatrixMode(GL.GL_MODELVIEW); // Select The Modelview Matrix
      gl.glLoadIdentity();
   }



   /**
    * Initializes the array of particles.
    */
   private void initVertexs() {
      // Create new particles for the array and ask each particle 
      // to initialize itself.
      for (int i = 0; i < MAX_PARTICLES; i++) {
         m_aVertexs[i] = new Vertex(m_oParent);
      }
   }

   /**
    * Draw the live particle using triangle strips.
    * 
    * @param gl The OpenGL reference.
    * @param particle The Vertex object to display.
    */
   private void handleLiveVertex(GL gl, Vertex particle) {
      // The current location of the particle; Need to account for the zoom
      // distance so user can zoom in and out the particles.
      float x = particle.getXLocation();
      float y = particle.getYLocation();
      float z = particle.getZLocation() + m_oParent.m_fZoom;
      texture.bind();
      texture.enable();
      gl.glPushMatrix();
      gl.glTranslatef(x, y, z);
      gl.glColor4f(
//         particle.getRed(),
//         particle.getGreen(),
//         particle.getBlue(),
         0.5f, 0.5f, 0.5f,
         0.8f);
      gl.glCallList(snowList);
      gl.glPopMatrix();
      updateVertex(particle);
   }

   /**
    * Updates the properties of the given particle. For example, its life,
    * location, etc. must be updated.
    * 
    * @param particle The Vertex to be updated.
    */
   private void updateVertex(Vertex particle) {
      // Update the particles's location. The particle's new location is
      // equal to it's current location + it's directional speed/slowdown.
      particle.setXLocation(
         particle.getXLocation()
            + particle.getXSpeed() / (m_oParent.m_fSlowDown * 1000));
      particle.setYLocation(
         particle.getYLocation()
            + particle.getYSpeed() / (m_oParent.m_fSlowDown * 1000));
      particle.setZLocation(
         particle.getZLocation()
            + particle.getZSpeed() / (m_oParent.m_fSlowDown * 1000));
      // Update the particle's directional speed. It's directional speed
      // is affected by the direction's gravitional pull.
      particle.setXSpeed(particle.getXSpeed() + particle.getXGravity() + m_oParent.m_fXSpeed);
      particle.setYSpeed(particle.getYSpeed() + particle.getYGravity() + m_oParent.m_fYSpeed);
      particle.setZSpeed(particle.getZSpeed() + particle.getZGravity());
      // Update the particle's X and Y gravitional pulls. The gravitational
      // pulls can be modified by the user.
      particle.setXGravity(
         particle.getXGravity() + m_oParent.m_fXGravityChange);
      particle.setYGravity(
         particle.getYGravity() + m_oParent.m_fYGravityChange);
   }

   /**
    * Handles the dead particle given. A dead particle is restarted by
    * given it full life, new color, new speeds, etc.
    * 
    * @param gl The OpenGL reference.
    * @param particle The dead particle to be processed.
    */
   private void handleDeadVertex(GL gl, Vertex particle) {
      // Ask the particle to restart itself.
      particle.setLife(particle.getLife() + 1.0f);
      particle.setYLocation(5.0f);
      if(particle.isAlive()){
          particle.reset();
      }
   }

   /**
    * Stops the Animator. 
    */
   protected void stopAnimator() {
      m_oAnimator.stop();
   }

   /**
    * Starts the Animator.
    */
   protected void startAnimator() {
      try {
         m_oAnimator.start();
      } catch (Exception e) {
         // Ignore error. Sometimes an exception will be thrown saying
         // the animator is already started.
      }
   }
}
