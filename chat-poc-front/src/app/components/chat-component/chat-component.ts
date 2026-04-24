import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, signal, WritableSignal   } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Reservation } from '../../core/interfaces/reservation';
import { Conversation } from '../../core/interfaces/conversation';
import { Message } from '../../core/interfaces/message';
import { ReservationService } from '../../core/services/reservationService';
import { ConversationService } from '../../core/services/conversationService';
import { ChatService } from '../../core/services/chatService';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../../core/interfaces/user';
import { UserService } from '../../core/services/userService';


@Component({
  selector: 'app-chat-component',
  imports: [TranslateModule, CommonModule, FormsModule ],
  templateUrl: './chat-component.html',
  styleUrl: './chat-component.css',
})
export class ChatComponent implements OnInit, OnDestroy {

  reservations = signal<Reservation[]>([]);
  selectedReservation = signal<Reservation | null>(null);
  currentConversation = signal<Conversation | null>(null);
  currentUser = signal<User | null>(null);
  messages = signal<Message[]>([]);
  newMessage: string = '';

  constructor(
    private reservationService: ReservationService,
    private conversationService: ConversationService,
    private chatService: ChatService,
    private userService: UserService
  ) {}



  ngOnInit(): void {

    this.userService.getMe().subscribe({
      next: (user) => this.currentUser.set(user),
      error: (err) => console.error(err)
    });
    this.reservationService.getMyReservations().subscribe({
      next: (res) => {
        console.log("Données reçues du serveur :", res);
        this.reservations.set(res);
      },
      error: (err) => console.error("Erreur de chargement des réservations", err)
    });

    this.chatService.messageSource.subscribe((msg) => {
      if (msg) {
        console.log("Nouveau message reçu via WebSocket :", msg);
        this.messages.update(prev => [...prev, msg]);
      }
    });
  }



  selectReservation(res: Reservation): void {
    this.chatService.disconnect();

    this.selectedReservation.set(res);
    
    this.conversationService.getConversationByReservation(res.id).subscribe({
      next: (conv: Conversation) => {
        this.currentConversation.set(conv);
        this.messages.set(conv.messages || []); // On charge l'historique
        
        // On ouvre le tuyau WebSocket pour CETTE conversation
        this.chatService.connect(conv.id);
      },
      error: (err : HttpErrorResponse) => console.error("Erreur de chargement du tchat", err)
    });
  }


  sendMessage(): void {
    if (this.newMessage.trim() && this.currentConversation()) {
      this.chatService.sendMessage(this.currentConversation()!.id, this.newMessage);
      this.newMessage = '';
    }
  }


  ngOnDestroy(): void {
    this.chatService.disconnect();
  }


}
