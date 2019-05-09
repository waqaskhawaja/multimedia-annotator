import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';
import { ISession } from 'app/shared/model/session.model';
import { SessionService } from 'app/entities/session';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-annotation-session-update',
    templateUrl: './annotation-session-update.component.html'
})
export class AnnotationSessionUpdateComponent implements OnInit {
    annotationSession: IAnnotationSession;
    isSaving: boolean;

    sessions: ISession[];

    users: IUser[];
    start: string;
    end: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected annotationSessionService: AnnotationSessionService,
        protected sessionService: SessionService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
            this.start = this.annotationSession.start != null ? this.annotationSession.start.format(DATE_TIME_FORMAT) : null;
            this.end = this.annotationSession.end != null ? this.annotationSession.end.format(DATE_TIME_FORMAT) : null;
        });
        this.sessionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISession[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISession[]>) => response.body)
            )
            .subscribe((res: ISession[]) => (this.sessions = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.annotationSession.start = this.start != null ? moment(this.start, DATE_TIME_FORMAT) : null;
        this.annotationSession.end = this.end != null ? moment(this.end, DATE_TIME_FORMAT) : null;
        if (this.annotationSession.id !== undefined) {
            this.subscribeToSaveResponse(this.annotationSessionService.update(this.annotationSession));
        } else {
            this.subscribeToSaveResponse(this.annotationSessionService.create(this.annotationSession));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnotationSession>>) {
        result.subscribe((res: HttpResponse<IAnnotationSession>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSessionById(index: number, item: ISession) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
