package de.adesso.anki.sdk.messages;

import java.util.EventListener;

public interface MessageListener<T extends IMessage> extends EventListener {
  public void messageReceived(T message);
}
