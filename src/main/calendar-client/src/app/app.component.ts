import { Component } from '@angular/core';
import { Router } from '@angular/router';


import { User } from './models/User';
import {AuthenticationService} from "./services/authentication.service";

@Component({ selector: 'app-root', templateUrl: 'app.component.html' })
export class AppComponent {
  currentUser!: User;

  constructor(private router: Router,
    private authenticationService: AuthenticationService) {
    this.authenticationService.currentUser.subscribe(result => this.currentUser = result);
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(result => this.currentUser = result);

  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  login() {
    this.router.navigate(['/login']);
  }
}
