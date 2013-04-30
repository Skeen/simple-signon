import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

class Utilities
{
    private static class ImageUtil
    {
        private static Map<String, Image> image_map = new HashMap<String, Image>();

        // Loads an image, or returns the cached one
        public static Image load(String path)
        {
            // Try to get the image from the map
            Image image = image_map.get(path);
            if(image == null)
            {
                try
                {
                    // Load it
                    image = ImageIO.read(new File(path));
                    // ... and add it to the map
                    image_map.put(path, image);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            // Return it
            return image;
        }
    }

    public static Image loadImage(String path)
    {
        return ImageUtil.load(path);
    }

    public static Image resizeImage(Image srcImage, int width, int height)
    {
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImage, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }

    public static Image overlayImage(Image background, Image overlay)
    {
        Dimension dim = new Dimension(background.getHeight(null), background.getWidth(null));
        // Creates the buffered image.
        BufferedImage buffImg1 = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = buffImg1.createGraphics();

        // Creates the buffered image.
        BufferedImage buffImg2 = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffImg2.createGraphics();

        // Clears the previously drawn image.
        g1.setColor(Color.white);
        g1.fillRect(0, 0, dim.width, dim.height);

        // Draws the rectangle and ellipse into the buffered image.
        g2.drawImage(background, null, null);

        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(dim.height-overlay.getHeight(null), dim.width-overlay.getWidth(null));
        g2.drawImage(overlay, transform, null);

        // Draws the buffered image.
        g1.drawImage(buffImg2, null, 0, 0);

        return buffImg2;
    }
}
