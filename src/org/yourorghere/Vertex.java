package org.yourorghere;



import java.util.Random;

/**
 * This class represents a Vertex object used in NeHe's lesson 19.  
 * 
 * @author Irene Kam
 */
public class Vertex {
   // This Vertex's life. Full life has the value 1.0f.
   private float life;
   // Red, green, and blue colors for this particle.
   private float red;
   private float green;
   private float blue;
   // X, Y, Z locations for this particle.
   private float xLocation = 0f;
   private float yLocation = 0f;
   private float zLocation = 0f;
   // X, Y, Z directions and speeds for this particle.
   private float xSpeed;
   private float ySpeed;
   private float zSpeed;
   // X, Y, Z gravity on this particle(direction and pull force).
   private float xGravity;
   private float yGravity;
   private float zGravity;
   private int index;
   private float angle;
   public static int MAX_PARTICLES;

   // The parent object.
   Snow m_oParent;
   public static Random generator;
   /**
    * Creates a Vertex instance with it's properties initialized.
    */
   public Vertex(Snow parent) {
      m_oParent = parent;
      generator = new Random();
      // Initialize the property values.
      reset();
   }

   protected void reset() {
      life = 1.0f;
      xLocation = getRandomxLocation();
      yLocation = 15f;
      zLocation = getRandomzLocation();
      // Assign direction and speed for this particle.
//      angle = (float)(2*3.14*100)/(MAX_PARTICLES);
      xSpeed = 30f;
      ySpeed = 0f;
      zSpeed = 0f;
      // Assign gravity. Initial gravity is pulling downwards (-y direction).
      xGravity = 0f;
      yGravity = -3.0f;
      zGravity = 0f;
   }
   
   private float getRandomxLocation() {
      
      return (float)generator.nextInt(300)/10f - 15f;
   }
   private float getRandomzLocation() {
      return (float)generator.nextInt(700)/10f - 50f;
   }
   public boolean isAlive() {
      if (yLocation > -8.0f && life > 0) {
         return true;
      } else {
         return false;
      }
   }

   public float getBlue() {
      return blue;
   }

   public float getGreen() {
      return green;
   }

   public float getLife() {
      return life;
   }

   public float getRed() {
      return red;
   }

   public float getXGravity() {
      return xGravity;
   }

   public float getXLocation() {
      return xLocation;
   }

   public float getXSpeed() {
      return xSpeed;
   }

   public float getYGravity() {
      return yGravity;
   }

   public float getYLocation() {
      return yLocation;
   }

   public float getYSpeed() {
      return ySpeed;
   }

   public float getZGravity() {
      return zGravity;
   }

   public float getZLocation() {
      return zLocation;
   }

   public float getZSpeed() {
      return zSpeed;
   }

   public void setBlue(float f) {
      blue = f;
   }

   public void setGreen(float f) {
      green = f;
   }

   public void setLife(float f) {
      if(f > 1.0)
           f = 1.0f;
      life = f;
   }

   public void setRed(float f) {
      red = f;
   }

   public void setXGravity(float f) {
      xGravity = f;
   }
   
   public void setXLocation(float f) {
      xLocation = f;
   }

   public void setXSpeed(float f) {
      xSpeed = f;
   }

   public void setYGravity(float f) {
      yGravity = f;
   }

   public void setYLocation(float f) {
      yLocation = f;
   }

   public void setYSpeed(float f) {
      ySpeed = f;
   }

   public void setZGravity(float f) {
      zGravity = f;
   }

   public void setZLocation(float f) {
      zLocation = f;
   }

   public void setZSpeed(float f) {
      zSpeed = f;
   }
   
   public void setIndex(int i) {
      index = i;
   }
}
