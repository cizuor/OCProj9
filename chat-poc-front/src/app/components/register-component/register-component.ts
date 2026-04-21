import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Authservice } from '../../services/authService';



@Component({
  selector: 'app-register-component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css',
})
export class RegisterComponent {
  registerData = {
    email: '',
    pseudo: '',
    password: ''
  };

  errorMessage = '';
  successMessage = '';


  constructor(private authService: Authservice, private router: Router) {}

  onRegister() {
    this.authService.register(this.registerData).subscribe({
      next: (res) => {
        this.successMessage = "Compte créé avec succès ! Redirection...";
        // On attend 2 secondes pour que l'utilisateur voie le message, puis redirection
        setTimeout(() => this.router.navigate(['/chat']), 2000);
      },
      error: (err) => {
        this.errorMessage = err.error || "Erreur lors de la création du compte.";
      }
    });
  }
}
