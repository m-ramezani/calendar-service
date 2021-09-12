import {Component, OnInit, TemplateRef, ViewChild,} from '@angular/core';
import {isSameDay, isSameMonth} from 'date-fns';
import {Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventTimesChangedEvent,
  CalendarEventTitleFormatter,
  CalendarView
} from 'angular-calendar';
import {utcToZonedTime} from "date-fns-tz";
import {UserEventService} from "../../services/user-event.service";
import {User} from "../../models/User";
import {FormBuilder, Validators} from "@angular/forms";
import {UserEvent} from "../../models/UserEvent";
import * as moment from "moment";
import {AuthenticationService} from "../../services/authentication.service";
import {EventMessageService} from "../../services/event-message.service";


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  providers: [
    {
      provide: CalendarEventTitleFormatter
    }
  ],
})
export class MainComponent implements OnInit {

  @ViewChild('createEvent', {static: true}) createEvent!: TemplateRef<any>;
  @ViewChild('deleteEvent', {static: true}) deleteEvent!: TemplateRef<any>;
  @ViewChild('updateEvent', {static: true}) updateEvent!: TemplateRef<any>;

  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;
  viewDate: Date = new Date();
  timezone: string = Intl.DateTimeFormat().resolvedOptions().timeZone;
  currentUser!: User;
  events: CalendarEvent[] = [];
  activeDayIsOpen: boolean = true;

  modalData: {
    action: string;
    event: CalendarEvent;
  } | undefined;
  refresh: Subject<any> = new Subject();

  newEventForm = this.formBuilder.group({
    title: ['', Validators.required],
    color: '',
    start: ['', Validators.required],
    end: ['', Validators.required],
    numberOfRepetition: 0
  })

  deleteEventForm = this.formBuilder.group({
    id: '',
    title: ['', Validators.required],
    color: '',
    start: ['', Validators.required],
    end: ['', Validators.required],
    originalEventId: '',
    deleteFollowingEvents: false
  });

  updateEventForm = this.formBuilder.group({
    id: '',
    title: ['', Validators.required],
    color: '',
    start: ['', Validators.required],
    end: ['', Validators.required],
    originalEventId: '',
    updateFollowingEvents: false
  })

  constructor(private modal: NgbModal,
              private userEventService: UserEventService,
              private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService,
              private messageService: EventMessageService) {
    this.authenticationService.currentUser.subscribe(result => this.currentUser = result);
  }

  ngOnInit(): void {
    this.fetchEvents();
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen) || events.length === 0);
      this.viewDate = date;
    }
  }

  fetchEvents(): void {
    if (this.currentUser !== undefined && this.currentUser.email) {
      this.userEventService.list(this.currentUser)
        .subscribe((serviceEvents) => {
            const mappedEvents: CalendarEvent[] = [];

            serviceEvents.forEach((event) => {
              mappedEvents.push({
                id: event.id,
                title: event.title,
                start: utcToZonedTime(new Date(event.start), this.timezone),
                end: utcToZonedTime(new Date(event.end), this.timezone),
                // actions: this.actions,
                color: {
                  primary: event.color,
                  secondary: event.color
                },
                meta: {
                  originalEventId: event.originalEventId,
                  userId: event.userId
                }
              })
            })

            this.events = mappedEvents;
          },
          (error: any) => {
            this.messageService.error("An error occurred fetching event: " + error.error.message);
          });
    }
  }

  handleEvent(action: string, event: CalendarEvent): void {
    switch (action) {
      case 'update':
        if (event) {
          this.updateEventForm.patchValue({
            id: event.id,
            title: event.title,
            color: event.color?.primary,
            start: event.start,
            end: event.end,
            originalEventId: event.meta.originalEventId
          });
          this.modalData = {event, action};
          this.modal.open(this.updateEvent, {size: 'lg', animation: true});
        }
        break;
      case 'remove':
        if (event) {
          this.deleteEventForm.patchValue({
            id: event.id,
            title: event.title,
            color: event.color?.primary,
            start: event.start,
            end: event.end,
            originalEventId: event.meta.originalEventId
          });
          this.deleteEventForm.disable();
          this.deleteEventForm.controls['deleteFollowingEvents'].enable();
          this.modalData = {event, action};
          this.modal.open(this.deleteEvent, {size: 'lg', animation: true});
        }
        break;
      default:
        break;
    }
  }

  addEvent(): void {
    this.modal.open(this.createEvent, {size: 'lg', animation: true});
  }

  createNewEvent(): void {
    this.messageService.clear();
    const event: UserEvent = {
      title: this.newEventForm.controls.title.value,
      userId: this.currentUser.id,
      start: moment(this.newEventForm.controls.start.value).format('yyyy-MM-DDTHH:mm'),
      end: moment(this.newEventForm.controls.end.value).format('yyyy-MM-DDTHH:mm'),
      color: this.newEventForm.controls.color.value,
      numberOfRepetition: this.newEventForm.controls.numberOfRepetition.value
    };

    if (this.datesValid(this.newEventForm.controls.start.value, this.newEventForm.controls.end.value)) {
      this.userEventService.create(this.currentUser, event).subscribe(() => {
          this.fetchEvents();
        },
        (error: any) => {
          this.messageService.error("An error occurred creating event: " + error.error.message);
        });
    }
  }

  submitUpdateEvent() {
    this.messageService.clear();
    const event: UserEvent = {
      id: this.updateEventForm.controls.id.value,
      title: this.updateEventForm.controls.title.value,
      userId: this.currentUser.id,
      color: this.updateEventForm.controls.color.value,
      start: moment(this.updateEventForm.controls.start.value).format('yyyy-MM-DDTHH:mm'),
      end: moment(this.updateEventForm.controls.end.value).format('yyyy-MM-DDTHH:mm'),
      originalEventId: this.updateEventForm.controls.originalEventId.value
    }
    if (this.datesValid(this.updateEventForm.controls.start.value, this.updateEventForm.controls.end.value)) {
      this.userEventService.update(this.currentUser, event, this.updateEventForm.controls.updateFollowingEvents.value).subscribe(() => {
          this.fetchEvents();
        },
        (error: any) => {
          this.messageService.error("An error occurred editing event: " + error.error.message);
        });
    }
  }

  submitRemoveEvent() {
    this.messageService.clear();
    this.userEventService.delete(this.currentUser, this.deleteEventForm.controls.id.value,
      this.deleteEventForm.controls.deleteFollowingEvents.value).subscribe(() => {
        this.fetchEvents();
      },
      (error: any) => {
        this.messageService.error("An error occurred deleting event: " + error.error.message);
      });
  }

  datesValid(start: any, end: any): boolean {
    this.messageService.clear();
    if (start < new Date()) {
      this.messageService.error("Start time must be greater then current date time");
      return false;
    }
    if (end < new Date()) {
      this.messageService.error("End time must be greater then current date time");
      return false;
    }
    if (start > end) {
      this.messageService.error("End time must be greater then start time");
      return false;
    }
    return true;
  }

  eventTimesChanged({event, newStart, newEnd,}: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
  }
}
