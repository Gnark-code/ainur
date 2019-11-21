package fr.gnark.sound.media;

public interface Output {
    void flush();

    void storeData(double levelLeft, double levelRight);

    long getThroughputInBytes();

    int getFrameSize();

}
