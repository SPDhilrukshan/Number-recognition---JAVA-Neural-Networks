package MNIST;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class MNISTSuper extends RandomAccessFile {
    private int count;


    public MNISTSuper(String name, String mode) throws IOException {
        super(name, mode);
        if (getMagicNumber() != readInt()) {
            throw new RuntimeException("");
        }
        count = readInt();
    }

    protected abstract int getMagicNumber();

    public long getCurrentIndex() throws IOException {
        return (getFilePointer() - getHeaderSize()) / getEntryLength() + 1;
    }

    public int getHeaderSize() {
        return 8;
    }

    public int getEntryLength() {
        return 1;
    }

    public void next() throws IOException {
        if (getCurrentIndex() < count) {
            skipBytes(getEntryLength());
        }
    }

    public void prev() throws IOException {
        if (getCurrentIndex() > 0) {
            seek(getFilePointer() - getEntryLength());
        }
    }


}
