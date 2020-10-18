These IMAGEJ plugins are made to extract discrete contours from pictures with different methods and are supposed to be used on grayscale images, like shown in the directory "img_tests".

-Masque_Sobel.java generates three gradient maps (along X, Y and XY) by applying three different sobel filters.

-Norme_Gradient.java computes the XY gradient map using the sobel filter and normalizes it between 0 and 255.

-Norme_Gradient_Seuil.java does the same as Norme_Gradient.java and applies a basic threshold to it.

-Seuillage_Hysteresis.java does the same as Norme_Gradient_Seuil.java but uses the hysteresis threshold.

-Supp_Maxima.java does the same as Norme_Gradient_Seuil.java but uses the supp maxima threshold.

To download IMAGEJ : https://github.com/imagej .
To execute these plugins, put them into the directory /home/.imagej/plugins, open an image in the software IMAGEJ, then click "plugins" -> "compile and run", search and select the plugin you want to use.
