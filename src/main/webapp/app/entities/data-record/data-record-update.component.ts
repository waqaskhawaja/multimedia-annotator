import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDataRecord } from 'app/shared/model/data-record.model';
import { DataRecordService } from './data-record.service';
import { ISession } from 'app/shared/model/session.model';
import { SessionService } from 'app/entities/session';
import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { InteractionTypeService } from 'app/entities/interaction-type';

@Component({
    selector: 'jhi-data-record-update',
    templateUrl: './data-record-update.component.html'
})
export class DataRecordUpdateComponent implements OnInit {
    dataRecord: IDataRecord;
    isSaving: boolean;

    sessions: ISession[];

    interactiontypes: IInteractionType[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected dataRecordService: DataRecordService,
        protected sessionService: SessionService,
        protected interactionTypeService: InteractionTypeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dataRecord }) => {
            this.dataRecord = dataRecord;
        });
        this.sessionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISession[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISession[]>) => response.body)
            )
            .subscribe((res: ISession[]) => (this.sessions = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.interactionTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IInteractionType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IInteractionType[]>) => response.body)
            )
            .subscribe((res: IInteractionType[]) => (this.interactiontypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.dataRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.dataRecordService.update(this.dataRecord));
        } else {
            this.subscribeToSaveResponse(this.dataRecordService.create(this.dataRecord));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataRecord>>) {
        result.subscribe((res: HttpResponse<IDataRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackInteractionTypeById(index: number, item: IInteractionType) {
        return item.id;
    }
}
