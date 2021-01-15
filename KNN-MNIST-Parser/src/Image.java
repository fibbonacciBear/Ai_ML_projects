import java.awt.*;
import java.awt.image.BufferedImage;

public class Image {

	private static final int PIXEL_THRESHOLD = 128;
	//private static final int PRETTY_RGB_PIXEL_COLOR = new Color(0, 255, 0).getRGB();

	protected int rows;
	protected int cols;
	protected int digit;
	public static int ID = 0;
	protected byte[][] pixels;
	public final int imageID;
	public double distance = 0;
	public int distanceID = -1;

	public Image(int rows, int cols) {
		this.pixels = new byte[rows][cols];
		this.rows = rows;
		this.cols = cols;
		imageID = ID;
		ID++;
	}

	public int rows() {
		return this.rows;
	}

	public int cols() {
		return this.cols;
	}

	public int digit() {
		return this.digit;
	}

	public void digit(int digit) {
		this.digit = digit;
	}

	public byte get(int row, int col) {
		return this.pixels[row][col];
	}

	public void set(int row, int col, byte value) {
		this.pixels[row][col] = value;
	}

	public byte[][] pixels() {
		return this.pixels;
	}

	public void pixels(byte[][] pixels) {
		this.pixels = pixels;
	}

	public boolean equals(Image other) {
		if (this.rows != other.rows) return false;
		if (this.cols != other.cols) return false;
		if (this.digit != other.digit) return false;
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				if (this.pixels[row][col] != other.pixels[row][col]) return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Image && equals((Image) other);
	}

	@Override
	public String toString() {
		String result = "";
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				int pixel = this.pixels[row][col];
				if (pixel < 0) pixel += 256;
				result += (pixel >= PIXEL_THRESHOLD) ? '*' : ' ';
				// result += String.format("%02x ", this.pixels[row][col]);
			}
			result += '\n';
		}
		return result;
	}

	public BufferedImage toBufferedImage() {
		BufferedImage returnValue = new BufferedImage(this.rows, this.cols, BufferedImage.TYPE_INT_RGB);

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				int pixel = this.pixels[row][col];
				if (pixel < 0) pixel += 256;

				returnValue.setRGB(col, row, (new Color(pixel, pixel, pixel).getRGB()));
			}
		}

		return returnValue;
	}

}
