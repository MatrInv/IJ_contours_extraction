// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;
import java.lang.Math.*;// pour classe GenericDialog et Newimage

public class Masque_Sobel implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth ();
		int h = ip.getHeight ();
		byte [] pixels = ( byte []) ip.getPixels ();
		
		ImagePlus result1 = NewImage.createByteImage (" Filtrage Sobel X ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr1 = result1.getProcessor ();
		byte [] pixelsr1 = ( byte []) ipr1.getPixels ();//image Sx
		
		ImagePlus result2 = NewImage.createByteImage (" Filtrage Sobel Y ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr2 = result2.getProcessor ();
		byte [] pixelsr2 = ( byte []) ipr2.getPixels ();//image Sy

		ImagePlus result3 = NewImage.createByteImage (" Filtrage Gradient ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr3 = result3.getProcessor ();
		byte [] pixelsr3 = ( byte []) ipr3.getPixels ();//image Sy + Sx
		
		int [][] masqueX = {{ 1, 2, 1}, {0, 0, 0}, {-1, -2, -1} };
		int [][] masqueY = {{ -1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
		int n = 1; // taille du demi - masque

		//Partie COMPLETEE
		
		int margeX = (int)(masqueX[0].length/2);
		int margeY = (int)(masqueX.length/2);
		
		int valX, valY,I, seuil=225;
		//int newI;
		
		
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

				/*newI = (int)Math.round(Math.sqrt((I*I*valX*valX)+(I*I*valY*valY)));*/

				pixelsr3 [x+y*w] = (byte)normalize((int)Math.abs(valX)+(int)Math.abs(valY));

				pixelsr2 [x+y*w] = (byte)normalize(valY);

				pixelsr1 [x+y*w] = (byte)normalize(valX);
			}
			
		//
			
		result1.show ();
		result1.updateAndDraw ();
		
		result2.show ();
		result2.updateAndDraw ();

		result3.show ();
		result3.updateAndDraw ();
		
	}
	
	public int normalize(int n){
	  int norm=n;
	  if(n>255)
	    norm=255;
	  if(n<0)
	    norm=0;
	    
	  return norm;
	}

}
