package image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class LoadImage {

    public static double[] ImageLoader(File input){

        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(input);
            Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders(imageInputStream);

            ImageReader imageReader = imageReaderIterator.next();

            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            bufferedImage = resizeImage(bufferedImage,28,28);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            double[][] image_buffer = new double[height][width];

            double[] inputData = new double[784];

            Raster raster = bufferedImage.getData();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {


                    System.out.println("i - " + i + " --------- j- " + j);

                    image_buffer[i][j] = (double) raster.getSample(i,j, 0) / (double)255;

                }
            }

            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    inputData[j*28 +i] = image_buffer[i][j];

                }
            }
            return inputData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static double[] ImageLoaderFromBufferedImage(BufferedImage bufferedImage){
        try {

            bufferedImage = resizeImage(bufferedImage,28,28);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            double[][] image_buffer = new double[height][width];
            double[][] image_buffer_D = new double[height][width];

            double[] inputData = new double[784];
            Raster raster = bufferedImage.getData();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image_buffer[i][j] = (double) raster.getSample(i,j, 0) / (double)255;
                }
            }

            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    inputData[j*28 +i] = image_buffer[i][j];
                }
                System.out.println(Arrays.toString(image_buffer[i]));
            }
            return inputData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage createImage(JPanel panel) {

        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = bi.createGraphics();
        panel.print(g);
        panel.paint(g);

        try{
            ImageIO.write(bi, "png", new File("imgg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.dispose();
        return bi;
    }


    static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

}
