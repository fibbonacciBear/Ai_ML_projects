import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class Reader {

	public int count;
	private int rows;
	private int cols;

	private InputStream images;
	private InputStream labels;

	public static class FileFormatException extends IOException {
		public FileFormatException(String message) {
			super(message);
		}
	}

	public Reader(String imageFileName, String labelFileName) throws IOException {
		this.images = new FileInputStream(imageFileName);
		this.labels = new FileInputStream(labelFileName);
		readImagesHeader();
		readLabelsHeader();
	}

	public int readInt(InputStream input) throws IOException {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			int b = input.read();
			result = 256 * result + b;
		}
		return result;
	}


	private void readImagesHeader() throws IOException {
		int magic = readInt(this.images);
		this.count = readInt(this.images);
		this.rows = readInt(this.images);
		this.cols = readInt(this.images);
	
		if (magic != 2051) throw new FileFormatException("Bad magic (images): " + magic);
		if (count <= 0) throw new FileFormatException("Invalid image count: " + count);
		if (rows <= 0) throw new FileFormatException("Invalid row size: " + rows);
		if (cols <= 0) throw new FileFormatException("Invalid col size: " + cols);
	}


	private void readLabelsHeader() throws IOException {
		int magic = readInt(this.labels);
		int count = readInt(this.labels);
	
		if (magic != 2049) throw new FileFormatException("Bad magic (labels): " + magic);
		if (count < this.count) throw new FileFormatException("Invalid label count: " + count);
	}
		
		
	private byte[][] readPixels() throws IOException {
		byte[][] result = new byte[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int pixel = images.read();
				if (pixel < 0) throw new FileFormatException("Invalid pixel: " + pixel);
				if (pixel > 255) throw new FileFormatException("Invalid pixel: " + pixel);
				result[row][col] = (byte) pixel;
			}
		}
		return result;
	}

	private int readLabel() throws IOException {
		int digit = labels.read();
		if (digit < 0) throw new FileFormatException("Invalid label: " + digit);
		if (digit > 9) throw new FileFormatException("Invalid label: " + digit);
		return digit;
	}

	public Image readImage() throws IOException {
		Image image = new Image(this.rows, this.cols);
		image.digit(readLabel());
		image.pixels(readPixels());
		return image;
		
	}

	public void skipImages(int count) throws IOException {
		images.skip(count * this.rows * this.cols);
		labels.skip(count);
	}

	public Image[] read(int max) throws IOException {
		Image[] images = new Image[Math.min(count, max)];
		for (int i = 0; i < images.length; i++) {
			if (i % 100 == 0) System.out.println("Reading image: " + i);
			images[i] = readImage();
		}
		return images;
	}

	public Image[] read() throws IOException {
		Image[] images = new Image[count];
		for (int i = 0; i < count; i++) {
			if (i % 100 == 0) System.out.println("Reading image: " + i);
			images[i] = readImage();
		}
		return images;
	}

	public static void main(String[] args) {
		String imageFile = "train-images.idx3-ubyte";
		String labelFile = "train-labels.idx1-ubyte";

		if (args.length > 0) imageFile = args[0];
		if (args.length > 1) labelFile = args[1];

		try {
			Reader reader = new Reader (imageFile, labelFile);
			if (args.length > 2) {

				// Display only those requested images.

				int previous = 0;
				for (int i = 2; i < args.length; i++) {
					int current = Integer.parseInt(args[i]);
					reader.skipImages(current - previous - 1);
					Image image = reader.readImage();
					System.out.println("Image of " + image.digit() + ":");
					System.out.println(image);
					previous = current; 
				}
			} else {

				// Display all images.

				Image[] images = reader.read();
				for (Image image : images) {
					System.out.println("Image of " + image.digit() + ":");
					System.out.println(image);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}

