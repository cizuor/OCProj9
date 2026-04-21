import { Message } from "./message";

export interface Conversation {
    id: number;
    reservationId: number;
    createdAt: string;
    updatedAt: string;
    messages: Message[]; 
}
