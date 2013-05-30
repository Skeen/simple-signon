import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import java.sql.*;

class Utilities
{
    public static File downloadResource(String path)
        throws Exception
    {
        String databaseString = MySQLConnection.DATABASE + ".`resource`";
        String updateString =   "SELECT resource_data FROM " + databaseString +
                                " where resource_path = ?";

        Connection connection = MySQLConnection.getSingleton().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateString);

        statement.setString(1, path);

        ResultSet r = statement.executeQuery();
        // Figure the number of rows
        int row_count = 0;
        while(r.next())
        {
            row_count++;
        }
        // Reset
        r.first();

        // We've got the row count
        if(row_count != 1)
        {
            throw new Exception("More than one row!!");
        }
        else
        {
            // Get the data, from the result set
            Blob blob_data = r.getBlob("resource_data");
            // Get a binary stream
            InputStream blob_data_stream = blob_data.getBinaryStream();

            File output_file = new File(path);
            // Make all needed directories
            File darent_directory = output_file.getParentFile();
            darent_directory.mkdirs();
            // Open a file writer, which overrides whatever was there before
            FileOutputStream output_writer = new FileOutputStream(output_file, false);
            // Write the Blobs data to the file
            IOUtils.copy(blob_data_stream, output_writer);
            // Close the file stream
            output_writer.close();
            // Return the file
            return output_file;
        }
    }

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
                    System.out.println("checking : " + path);

                    File image_file = new File(path);
                    // If the file doesn't exist, lets download it
                    if(image_file.exists() == false)
                    {
                        System.out.println("downloading : " + path);
                        // Download the file, and set the image variable
                        image_file = Utilities.downloadResource(path);
                    }
                    // If no exceptions has happen, we got a valid file now.
                    // So let's load it!
                    image = ImageIO.read(image_file);
                    // ... and add it to the map
                    image_map.put(path, image);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.exit(-1);
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

    public static int getRandomBetween(int min, int max)
    {
        Random rand = new Random();
        return min + rand.nextInt(max - min + 1);
    }

    public static void delay(int amount)
    {
        try
        {
            // Just sleep
            Thread.sleep(amount);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
