import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';
import { Message } from '../interfaces/message';

@Injectable({
  providedIn: 'root',
})
export class ChatService {

  private stompClient: Client | null = null;

  public messageSource = new BehaviorSubject<Message | null>(null);

  constructor() {}

  connect(conversationId: number) {
    const socket = new SockJS('http://localhost:8080/ws-chat');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connecté au WebSocket  pour la conversation : ' +conversationId);
      this.stompClient?.subscribe(`/topic/chat/${conversationId}`, (message) => {
        this.messageSource.next(JSON.parse(message.body));
      });
    };

    this.stompClient.activate();
  }

  sendMessage(conversationId: number, content: string) {
    const chatMessage = {
      contenu: content,
      conversationId: conversationId
    };
    this.stompClient?.publish({
      destination: '/app/send-message',
      body: JSON.stringify(chatMessage)
    });
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

}
