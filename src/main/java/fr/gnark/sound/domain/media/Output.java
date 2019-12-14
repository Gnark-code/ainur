package fr.gnark.sound.domain.media;

public interface Output {
    void flush();

    void storeData(double levelLeft, double levelRight);

    long getThroughputInBytes();

    int getFrameSize();

    byte[] getBuffer();
}
