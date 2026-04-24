import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
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

    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws-chat',
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connecté au WebSocket  pour la conversation : ' +conversationId);
      this.stompClient?.subscribe(`/topic/chat/${conversationId}`, (sdkEvent) => {
        const newMessage: Message = JSON.parse(sdkEvent.body);
        this.messageSource.next(newMessage);
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Erreur STOMP : ' + frame.headers['message']);
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
