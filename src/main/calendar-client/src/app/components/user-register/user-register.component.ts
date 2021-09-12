import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {EventMessageService} from "../../services/event-message.service";
import {UserRegisterService} from "../../services/user-register.service";
import {first} from "rxjs/operators";


@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {

  loading: boolean = false;
  registerForm: FormGroup = this.formBuilder.group({
    email: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private userRegisterService: UserRegisterService,
    private messageService: EventMessageService) {
    if (this.authenticationService.currentUserValue && this.authenticationService.currentUserValue.email) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
  }

  get form() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.messageService.clear();
    if (this.registerForm.valid) {
      this.loading = true;
      this.userRegisterService.register(this.registerForm.value)
        .pipe(first())
        .subscribe(
          (result: any) => {
            this.messageService.success('User has been registered successfully');
            this.router.navigate(['/login']);
          },
          (error: any) => {
            this.messageService.error("An error occurred while registering: " + error.error.message);
            this.loading = false;
          });
    }
    return;
  }

}
