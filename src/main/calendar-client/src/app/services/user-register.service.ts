import { Injectable } from '@angular/core';
import {User} from "../models/User";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class UserRegisterService {

  constructor(private http: HttpClient) { }


  apiUrl: string =  environment.baseUrl + '/api/user/';

  register(user: User) {
    return this.http.post(this.apiUrl + "register", user);
  }
}
