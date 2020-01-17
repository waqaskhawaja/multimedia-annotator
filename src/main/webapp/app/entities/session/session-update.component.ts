import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISession } from 'app/shared/model/session.model';
import { SessionService } from './session.service';
import { IDataType } from 'app/shared/model/data-type.model';
import { DataTypeService } from 'app/entities/data-type';
import { IScenario } from 'app/shared/model/scenario.model';
import { ScenarioService } from 'app/entities/scenario';

@Component({
    selector: 'jhi-session-update',
    templateUrl: './session-update.component.html'
})
export class SessionUpdateComponent implements OnInit {
    session: ISession;
    isSaving: boolean;

    datatypes: IDataType[];

    scenarios: IScenario[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected sessionService: SessionService,
        protected dataTypeService: DataTypeService,
        protected scenarioService: ScenarioService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ session }) => {
            this.session = session;
        });
        this.dataTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDataType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDataType[]>) => response.body)
            )
            .subscribe((res: IDataType[]) => (this.datatypes = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.scenarioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IScenario[]>) => mayBeOk.ok),
                map((response: HttpResponse<IScenario[]>) => response.body)
            )
            .subscribe((res: IScenario[]) => (this.scenarios = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.session.id !== undefined) {
            this.subscribeToSaveResponse(this.sessionService.update(this.session));
        } else {
            this.subscribeToSaveResponse(this.sessionService.create(this.session));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISession>>) {
        result.subscribe((res: HttpResponse<ISession>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDataTypeById(index: number, item: IDataType) {
        return item.id;
    }

    trackScenarioById(index: number, item: IScenario) {
        return item.id;
    }
}
