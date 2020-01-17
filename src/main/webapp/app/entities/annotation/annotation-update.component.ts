import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IAnnotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from './annotation.service';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from 'app/entities/annotation-session';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';
import { InteractionRecordService } from 'app/entities/interaction-record';

@Component({
    selector: 'jhi-annotation-update',
    templateUrl: './annotation-update.component.html'
})
export class AnnotationUpdateComponent implements OnInit {
    annotation: IAnnotation;
    isSaving: boolean;

    annotationsessions: IAnnotationSession[];

    interactionrecords: IInteractionRecord[];
    start: string;
    end: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected annotationService: AnnotationService,
        protected annotationSessionService: AnnotationSessionService,
        protected interactionRecordService: InteractionRecordService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotation }) => {
            this.annotation = annotation;
            this.start = this.annotation.start != null ? this.annotation.start.format(DATE_TIME_FORMAT) : null;
            this.end = this.annotation.end != null ? this.annotation.end.format(DATE_TIME_FORMAT) : null;
        });
        this.annotationSessionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnnotationSession[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnnotationSession[]>) => response.body)
            )
            .subscribe(
                (res: IAnnotationSession[]) => (this.annotationsessions = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.interactionRecordService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IInteractionRecord[]>) => mayBeOk.ok),
                map((response: HttpResponse<IInteractionRecord[]>) => response.body)
            )
            .subscribe(
                (res: IInteractionRecord[]) => (this.interactionrecords = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.annotation.start = this.start != null ? moment(this.start, DATE_TIME_FORMAT) : null;
        this.annotation.end = this.end != null ? moment(this.end, DATE_TIME_FORMAT) : null;
        if (this.annotation.id !== undefined) {
            this.subscribeToSaveResponse(this.annotationService.update(this.annotation));
        } else {
            this.subscribeToSaveResponse(this.annotationService.create(this.annotation));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnotation>>) {
        result.subscribe((res: HttpResponse<IAnnotation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAnnotationSessionById(index: number, item: IAnnotationSession) {
        return item.id;
    }

    trackInteractionRecordById(index: number, item: IInteractionRecord) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
