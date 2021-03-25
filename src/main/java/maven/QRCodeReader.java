package maven;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javax.imageio.ImageIO;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class QRCodeReader {

    private static String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }

    public static void main(String[] args) {
//        try {
//            File file = new File("src/test/resources/qr.png");
//            String decodedText = decodeQRCode(file);
//            if(decodedText == null) {
//                System.out.println("No QR Code found in the image");
//            } else {
//                System.out.println("Decoded text = " + decodedText);
//            }
//        } catch (IOException e) {
//            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
//        }
//    }
    	Webcam webcam = Webcam.getDefault(); // non-default (e.g. USB) webcam can be used too
    	webcam.open();

    	Result result = null;
    	BufferedImage image = null;
    	while(true) {
    	if (webcam.isOpen()) {
    	    if ((image = webcam.getImage()) == null) {
    	        continue;
    	    }
    	    
    	    LuminanceSource source = new BufferedImageLuminanceSource(image);
    	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    	    try {
    	        result = new MultiFormatReader().decode(bitmap);
    	    } catch (NotFoundException e) {
    	        System.out.println("Please hold the QR Code correctly into the Webcam");
    	        try{
    	            Thread.sleep(2000);
    	       }catch(InterruptedException ex){}
    	    }
    	}

    	if (result != null) {
    		 System.out.println("QR code data is: " + result.getText());
    		 if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    			    try {
						Desktop.getDesktop().browse(new URI(result.getText()));
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		 break;
    	}}
    }
}