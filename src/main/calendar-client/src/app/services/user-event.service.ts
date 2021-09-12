import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../models/User";
import {Observable} from "rxjs";
import {UserEvent} from "../models/UserEvent";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserEventService {

  private apiUrl = environment.baseUrl + '/api/event/';

  constructor(private http: HttpClient) {
  }

  create(user: User, event: UserEvent): Observable<void> {
    return this.http.post<void>(this.apiUrl + "create", event, {headers: this.makeHeaders(user)});
  }

  update(user: User, event: UserEvent, updateFollowingEvents: boolean): Observable<void> {
    return this.http.put<void>(this.apiUrl + "update", {
        id: event.id, title: event.title, color: event.color, start: event.start,
        end: event.end, userId: user.id, updateFollowingEvents: updateFollowingEvents
      },
      {headers: this.makeHeaders(user)});
  }

  delete(user: User, eventId: any, deleteFollowingEvents: boolean): Observable<void> {
    return this.http.delete<void>(this.apiUrl + "delete", {
      body: {id: eventId, deleteFollowingEvents: deleteFollowingEvents},
      headers: this.makeHeaders(user)
    });
  }

  list(user: User): Observable<UserEvent[]> {
    return this.http.get<UserEvent[]>(this.apiUrl + user.id, {headers: this.makeHeaders(user)});
  }

  makeHeaders(user: User): HttpHeaders {
    const authorizationKey: string = 'Basic ' + btoa(user.email + ':' + user.password);
    return new HttpHeaders({Authorization: authorizationKey});
  }
}
