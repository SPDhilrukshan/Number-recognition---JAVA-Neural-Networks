package MNIST;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MNISTImage extends MNISTSuper {
    private int rows;
    private int cols;

    public MNISTImage(String name, String mode) throws FileNotFoundException, IOException {
        super(name, mode);

        // read header information
        rows = readInt();
        cols = readInt();
    }

    @Override
    protected int getMagicNumber() {
        return 2051;
    }

    @Override
    public int getEntryLength() {
        return cols * rows;
    }

    @Override
    public int getHeaderSize() {
        return super.getHeaderSize() + 8;
    }
}
