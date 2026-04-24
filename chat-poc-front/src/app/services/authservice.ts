import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';



export interface LoginRequest {
  identifiant: string;
  password:  string;
}


export interface RegisterRequest {
  email: string;
  pseudo: string;
  password:  string;
}

export interface AuthResponse {
  token: string;
}


@Injectable({
  providedIn: 'root',
})
export class Authservice {
  private apiUrl = 'http://localhost:8080/api/auth';
  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest) {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.handleAuthentication(response))
    );
  }

  logout() {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken() {
    return localStorage.getItem('token');
  }

  register(userData: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => {this.handleAuthentication(response)})
    );
  }


  private handleAuthentication(res: AuthResponse) {
    if (res.token) {
      localStorage.setItem('token', res.token);
    }
  }


}
