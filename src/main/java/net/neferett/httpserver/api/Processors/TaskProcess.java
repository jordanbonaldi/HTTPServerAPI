package net.neferett.httpserver.api.Processors;

public interface TaskProcess extends Runnable{
    void start();
    void end();
}
