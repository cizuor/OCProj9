import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router,RouterLink  } from '@angular/router';
import { Authservice } from '../../services/authservice';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  loginData = { identifiant: '', password: '' };
  errorMessage = '';

  constructor(private authService: Authservice, private router: Router) {}

  onLogin() {
    this.authService.login(this.loginData).subscribe({
      next: (res) => {
        // Succès : Le Token est déjà stocké par le service, on change de page
        this.router.navigate(['/chat']);
      },
      error: (err) => {
        this.errorMessage = "Identifiants incorrects. Veuillez réessayer.";
      }
    });
  }

}
