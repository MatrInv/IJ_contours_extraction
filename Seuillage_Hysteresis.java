// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;
import java.lang.Math.*;// pour classe GenericDialog et Newimage

public class Seuillage_Hysteresis implements PlugInFilter {

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

		int accepte[][] = new int[h][w];
		
		int margeX = (int)(masqueX[0].length/2);
		int margeY = (int)(masqueX.length/2);
		
		int valX, valY,I; 
		int Sb=30, Sh=140;
		
		
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


				  int norm = normalize(Sb,(int)Math.abs(valX)+(int)Math.abs(valY),Sh,accepte[y][x]);
				  if(norm==255)//si (x,y) est accepté
				  //on met à jour tous les voisins à accepté
					  for(int j=-1;j<=-1;j++)
					  	for(int i=-1;i<=-1;i++)
					  		accepte[y+j][x+i] = 1;

				  pixelsr [x+y*w] = (byte)norm;


			}
			
		//

		result.show ();
		result.updateAndDraw ();
	}
	
	//je fais la normalisation et le seuillage là dedans
	public int normalize(int inf, int n, int sup, int contour){
	  int norm=n;
	  if(n>sup)
	    norm=255;
	  else if(n<inf)
	    norm=0;
	  else{
	  	if(contour!=0)
	  		norm=255;
	  	else
	  		norm=0;
	  }
	    
	  return norm;
	}

}
