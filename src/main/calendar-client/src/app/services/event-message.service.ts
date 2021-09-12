import { Injectable } from '@angular/core';
import {Observable, Subject} from "rxjs";
import {NavigationStart, Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class EventMessageService {

  private subject = new Subject<any>();
  private saveRouteTrack = false;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.saveRouteTrack) {
          this.saveRouteTrack = false;
        } else {
          this.clear();
        }
      }
    });
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

  success(message: string) {
    this.saveRouteTrack = true;
    this.subject.next({ type: 'success', text: message });
  }

  error(message: string) {
    this.saveRouteTrack = true;
    this.subject.next({ type: 'error', text: message });
  }

  clear() {
    this.subject.next();
  }
}
