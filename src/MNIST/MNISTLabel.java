package MNIST;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MNISTLabel extends MNISTSuper {

    public MNISTLabel(String name, String mode) throws IOException {
        super(name, mode);
    }

    public int readLabel() throws IOException {
        return readUnsignedByte();
    }

    @Override
    protected int getMagicNumber() {
        return 2049;
    }
}
