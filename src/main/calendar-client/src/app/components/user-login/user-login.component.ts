import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {EventMessageService} from "../../services/event-message.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  loading: boolean = false;

  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private messageService: EventMessageService) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get form() { return this.loginForm.controls; }

  onSubmit() {
    this.messageService.clear();
    if (this.loginForm.valid) {
      this.loading = true;
      this.authenticationService.login(this.loginForm.value)
        .pipe(first())
        .subscribe(
          (result: any) => {
            this.router.navigate(["/"]);
          },
          (error: any) => {
            this.messageService.error("An error occurred while login: " + error.error.message);
            this.loading = false;
          });
    }
   return;
  }
}
