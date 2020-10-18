// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;
import java.lang.Math.*;// pour classe GenericDialog et Newimage

public class Norme_Gradient_Seuil implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth ();
		int h = ip.getHeight ();
		byte [] pixels = ( byte []) ip.getPixels ();
		
		
		ImagePlus result = NewImage.createByteImage ("Filtrage : Norme Gradient avec Seuillage", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor ();
		byte [] pixelsr = ( byte []) ipr.getPixels ();//image gradient
		
		int [][] masqueX = {{ 1, 2, 1}, {0, 0, 0}, {-1, -2, -1} };
		int [][] masqueY = {{ -1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
		int n = 1; // taille du demi - masque

		//Partie COMPLETEE
		
		int margeX = (int)(masqueX[0].length/2);
		int margeY = (int)(masqueX.length/2);
		
		int valX, valY,I,newI, seuil=129;
		
		
		for (int y = margeY; y < h-margeY; y++)
			for ( int x = margeX; x < w-margeX; x++) {
				valX=0;
				valY=0;
				I=ip.get(x,y);
				for(int j=-margeY;j<margeX+1;j++)
				  for(int i=-margeX;i<margeX+1;i++){
				    valX+=masqueX[j+margeY][i+margeX]*(int)(pixels[x+i+(y+j)*w] & 0xff);
				    valY+=masqueY[j+margeY][i+margeX]*(int)(pixels[x+i+(y+j)*w] & 0xff); 
				  }

				  /*valX = normalize(valX,seuil);
				  valY = normalize(valY,seuil);
				  newI = (int)Math.round(Math.sqrt((I*I*valX*valX)+(I*I*valY*valY)));
				  pixelsr [x+y*w] = (byte)normalize(newI , seuil);*/

				  pixelsr [x+y*w] = (byte)normalize((int)Math.abs(valX)+(int)Math.abs(valY),seuil);

			}
			
		//

		result.show ();
		result.updateAndDraw ();
	}
	
	public int normalize(int n, int seuil){
	  int norm=n;
	  if(n>seuil)
	    norm=255;
	  if(n<seuil)
	    norm=0;
	    
	  return norm;
	}

}
