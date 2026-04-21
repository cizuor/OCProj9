import { User } from "./user";
import { Voiture } from "./voiture";

export interface Reservation {
    id: number;
    createdAt: string;
    updatedAt: string;
    dateDebut: string; 
    dateFin: string;
    prixTotal: number;
    client: User;
    voiture: Voiture;
}
