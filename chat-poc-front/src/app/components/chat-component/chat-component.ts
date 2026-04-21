import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Reservation } from '../../core/interfaces/reservation';
import { Conversation } from '../../core/interfaces/conversation';
import { Message } from '../../core/interfaces/message';
import { ReservationService } from '../../core/services/reservationService';
import { ConversationService } from '../../core/services/conversationService';
import { ChatService } from '../../core/services/chatService';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-chat-component',
  imports: [TranslateModule, CommonModule, FormsModule ],
  templateUrl: './chat-component.html',
  styleUrl: './chat-component.css',
})
export class ChatComponent implements OnInit, OnDestroy {

  reservations: Reservation[] = [];
  selectedReservation: Reservation | null = null;
  currentConversation: Conversation | null = null;
  messages: Message[] = [];
  newMessage: string = '';

  constructor(
    private reservationService: ReservationService,
    private conversationService: ConversationService,
    private chatService: ChatService
  ) {}



  ngOnInit(): void {
    this.reservationService.getMyReservations().subscribe({
      next: (res) => this.reservations = res,
      error: (err) => console.error("Erreur de chargement des réservations", err)
    });

    this.chatService.messageSource.subscribe((msg) => {
      if (msg) {
        this.messages.push(msg); 
      }
    });
  }



  selectReservation(res: Reservation): void {
    this.chatService.disconnect();

    this.selectedReservation = res;
    
    this.conversationService.getConversationByReservation(res.id).subscribe({
      next: (conv: Conversation) => {
        this.currentConversation = conv;
        this.messages = conv.messages || []; // On charge l'historique
        
        // On ouvre le tuyau WebSocket pour CETTE conversation
        this.chatService.connect(conv.id);
      },
      error: (err : HttpErrorResponse) => console.error("Erreur de chargement du tchat", err)
    });
  }


  sendMessage(): void {
    if (this.newMessage.trim() && this.currentConversation) {
      this.chatService.sendMessage(this.currentConversation.id, this.newMessage);
      this.newMessage = '';
    }
  }


  ngOnDestroy(): void {
    this.chatService.disconnect();
  }


}
