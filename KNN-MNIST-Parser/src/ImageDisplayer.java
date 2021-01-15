import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageDisplayer extends JPanel {
    Image[] images;
    BufferedImage image;
    BoundingBox imageBoundingBox;

    int currentImage = 0;

    class BoundingBox {
        int x;
        int y;
        int w;
        int h;
    }

    public ImageDisplayer(int images, int currentImage) throws IOException {
        Reader reader = new Reader("train-images.idx3-ubyte", "train-labels.idx1-ubyte");

        this.images = reader.read(images);

        changeImage(currentImage);
    }

    public void changeImage(int imageNum) {
        if(imageNum >= 0 && imageNum < this.images.length) {
            currentImage = imageNum;

            System.out.println(this.images[this.currentImage]);

            image = this.images[this.currentImage].toBufferedImage();
            imageBoundingBox = findBoundingBox(0, 0, this.images[this.currentImage]);
        }
    }

    public BoundingBox findBoundingBox(int initialX, int initialY, Image boundingImage) {
        BoundingBox returnValue = new BoundingBox();

        int topLeftPointX = 10000;
        int topLeftPointY = 10000;
        int bottomRightPointX = -1;
        int bottomRightPointY = -1;

        // find first top left most white point
        for(int y = 0; y < boundingImage.rows(); ++y) {
            for(int x = 0; x < boundingImage.cols(); ++x) {
                if (boundingImage.get(y, x) > 0) {
                    if(x < topLeftPointX) {
                        topLeftPointX = initialX + x;
                    }
                    if(y < topLeftPointY) {
                        topLeftPointY = initialY + y;
                    }
                }
            }
        }

        // find first bottom right most white point
        for(int y = boundingImage.rows() - 1; y >= 0; --y) {
            for(int x = boundingImage.cols() - 1; x >= 0; --x) {
                if (boundingImage.get(y, x) > 0) {
                    if(x > bottomRightPointX) {
                        bottomRightPointX = initialX + x;
                    }
                    if(y > bottomRightPointY) {
                        bottomRightPointY = initialY + y;
                    }
                }
            }
        }

        returnValue.x = topLeftPointX;
        returnValue.y = topLeftPointY;
        returnValue.w = bottomRightPointX - topLeftPointX;
        returnValue.h = bottomRightPointY - topLeftPointY;

        return returnValue;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.scale(10, 10);

        g2.drawImage(image, 0, 0, null);

        g2.setColor(Color.GREEN);

        g2.drawRect(imageBoundingBox.x, imageBoundingBox.y, imageBoundingBox.w, imageBoundingBox.h);
    }

    public static void main(String[] args) throws IOException {
        JFrame imageFrame = new JFrame();
        ImageDisplayer imageDisplayer = new ImageDisplayer(100, 0);

        imageFrame.setTitle("what an absolute meme");
        imageFrame.setSize(640, 480);
        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imageFrame.setVisible(true);

        imageFrame.add(imageDisplayer);
        imageFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                //
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    imageDisplayer.changeImage(imageDisplayer.currentImage + 1);
                } else if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    imageDisplayer.changeImage(imageDisplayer.currentImage - 1);
                }
                imageDisplayer.validate();
                imageDisplayer.repaint();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //
            }
        });
    }
}

