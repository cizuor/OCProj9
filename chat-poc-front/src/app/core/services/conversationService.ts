import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Conversation } from '../interfaces/conversation';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ConversationService {
   private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) {}


  getMyConversations(): Observable<Conversation[]> {
    return this.http.get<Conversation[]>(`${this.apiUrl}/me`);
  }
}
