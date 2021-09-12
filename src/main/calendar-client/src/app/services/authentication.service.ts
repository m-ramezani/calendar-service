import {Injectable} from '@angular/core';
import {User} from "../models/User";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<any>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser') || '{}'));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  apiUrl: string = environment.baseUrl + '/api/user/';

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  login(user: User) {

    const authorizationKey: string = 'Basic ' + btoa(user.email + ':' + user.password)
    const headers = new HttpHeaders({Authorization: authorizationKey});

    return this.http.post<any>(this.apiUrl + "authenticate", user, {headers})
      .pipe(map((data) => {
        const result: User = {
          email: user.email,
          password: user.password,
          id: data.id
        };
        localStorage.setItem('currentUser', JSON.stringify(result));
        this.currentUserSubject.next(result);
        return result;
      }));
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
