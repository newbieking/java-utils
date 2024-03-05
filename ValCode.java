package indi.newbieking;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

// Web code for the form post validating
class ValCode {


    /**
     * generate a code with 4 chars.
     *
     * @param formatName a String containing the informal name of the format.
     * @param output an OutputStream to be written to.
     * @return the code generated
     */
    public static char[] genCode(String formatName, OutputStream output) {
        BufferedImage bufferedImage = new BufferedImage(300, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = ((Graphics2D) bufferedImage.getGraphics());
        char[] words = draw(graphics, bufferedImage.getWidth(), bufferedImage.getHeight());
        try {
            ImageIO.write(bufferedImage, "jpg", output);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            graphics.dispose();
        }
        return words;
    }

    private static char[] draw(Graphics2D g, int width, int height) {
        g.setBackground(Color.GRAY);
        g.clearRect(0, 0, width, height);
        Random random = new Random();
        int lineCount = random.nextInt(10, 15);
        char[] words = new char[4];
        for (int i = 0; i < 4; i++) {
            char world = (char) random.nextInt('A', 'Z' + 1);
            words[i] = world;
        }
        int step = width / 4;
        for (int i = 0; i < words.length; i++) {
            Font font = g.getFont();
            Font f = new Font(font.getName(), Font.PLAIN, step);
            g.setFont(f);
            float dx = random.nextFloat(step / 2f);
            float y = random.nextFloat(step, height);
            System.out.printf("%c, %f, %f\n", words[i], i * step + dx, y);
            g.drawString(String.valueOf(words[i]), i * step + dx, y);
        }

        for (int i = 0; i < lineCount; i++) {
            int r = random.nextInt(256);
            int gg = random.nextInt(256);
            int b = random.nextInt(256);
            g.setColor(new Color(r, gg, b));
            g.setStroke(new BasicStroke(random.nextFloat(step / 10f)));
            g.drawLine(0, random.nextInt(height), width, random.nextInt(height));
        }
        return words;

    }


}
