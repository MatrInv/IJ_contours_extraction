// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;
import java.lang.Math.*;// pour classe GenericDialog et Newimage

public class Supp_Maxima implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth ();
		int h = ip.getHeight ();
		byte [] pixels = ( byte []) ip.getPixels ();
		
		
		ImagePlus result = NewImage.createByteImage ("Filtrage : Norme Gradient", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor ();
		byte [] pixelsr = ( byte []) ipr.getPixels ();//image gradient
		
		int [][] masqueX = {{ 1, 2, 1}, {0, 0, 0}, {-1, -2, -1} };
		int [][] masqueY = {{ -1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
		int n = 1; // taille du demi - masque

		//Partie COMPLETEE
		
		int margeX = (int)(masqueX[0].length/2);
		int margeY = (int)(masqueX.length/2);
		
		int valX, valY, I;
		
		double gradAngle[][] = new double[h][w];
		int gradNorm[][] = new int[h][w];
		
		//Remplissage de la table des normes et de la table des directions
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
				  
				gradAngle[y][x] = Math.atan2((double)valX,(double)valY);
				gradNorm[y][x] = normalize((int)Math.abs(valX)+(int)Math.abs(valY));

			}
			
		for (int y = margeY; y < h-margeY; y++)
			for ( int x = margeX; x < w-margeX; x++) {
				double theta = gradAngle[y][x];
				if(gradNorm[y][x]<gradNorm[y+dirY(theta)][x+dirX(theta)] || gradNorm[y][x]<gradNorm[y-dirY(theta)][x-dirX(theta)]) //si (x,y) n'est pas un maximum local
					pixelsr [x+y*w] = (byte)0; //on l'élimine de l'image
				else
					pixelsr [x+y*w] = (byte)gradNorm[y][x]; 
			}

		result.show ();
		result.updateAndDraw ();
	}
	
	public int normalize(int n){

		int norm=n;
	  	if(n>255)
	    	norm=255;
	  	if(n<0)
	    	norm=0;

		return norm;
	}


//fonction qui renvoit la valeur x discréte de t
	public int dirX(double t){
		double pi = Math.PI;
		double deg = t<0. ? (2.*pi)-t : t;
		double qrtpi = pi/4.;

		if((t<qrtpi) || (t>7.*qrtpi))
			return 1;
		else if ((t>3.*qrtpi) && (t<5.*qrtpi))
			return -1;

		return 0;
	}

//fonction qui renvoit la valeur y discréte de t
	public int dirY(double t){
		double pi = Math.PI;
		double deg = t<0. ? (2.*pi)-t : t;
		double qrtpi = pi/4.;

		if((t>qrtpi) || (t<3.*qrtpi))
			return 1;
		else if ((t>5.*qrtpi) && (t<7.*qrtpi))
			return -1;

		return 0;
	}
}
