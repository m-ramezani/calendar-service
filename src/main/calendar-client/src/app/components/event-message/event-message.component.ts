import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {EventMessageService} from "../../services/event-message.service";

@Component({
  selector: 'app-event-message',
  templateUrl: './event-message.component.html',
  styleUrls: ['./event-message.component.css']
})
export class EventMessageComponent implements OnInit {

  private subscription!: Subscription;
  message: any;
  success: boolean = false;

  constructor(private messageService: EventMessageService) {
  }

  ngOnInit() {
    this.subscription = this.messageService.getMessage()
      .subscribe(message => {
        this.success = message && message.type === 'success'
        this.message = message;
      });
  }
}
