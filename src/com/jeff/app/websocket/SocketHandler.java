package com.jeff.app.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.jeff.app.channel.ChannelHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {
		ChannelHandler channelHandler = new ChannelHandler();
		channelHandler.doTasks(message.getPayload());
		session.sendMessage(new TextMessage(channelHandler.channelUrl));
	}
}
