package de.adesso.anki.sdk.messages;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ResponseListener<T extends Message> implements MessageListener<T> {
  
  private boolean active;
  private BlockingQueue<T> queue;
  
  public ResponseListener() {
    this.queue = new LinkedBlockingQueue<>();
    this.active = true;
  }
  
  @Override
  public void messageReceived(T message) {
    if (active) {
      active = false;
      queue.add(message);
    }
  }
  
  public T awaitMessage(long timeout) {
    try {
      T message = queue.poll(timeout, TimeUnit.MILLISECONDS);
      return message;
    } catch (InterruptedException e) {
      return null;
    }   
  }

}
