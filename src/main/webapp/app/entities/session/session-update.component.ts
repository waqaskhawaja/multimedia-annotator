import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISession } from 'app/shared/model/session.model';
import { SessionService } from './session.service';
import { IAnalyst } from 'app/shared/model/analyst.model';
import { AnalystService } from 'app/entities/analyst';
import { IScenario } from 'app/shared/model/scenario.model';
import { ScenarioService } from 'app/entities/scenario';
import { IData } from 'app/shared/model/data.model';
import { DataService } from 'app/entities/data';

@Component({
    selector: 'jhi-session-update',
    templateUrl: './session-update.component.html'
})
export class SessionUpdateComponent implements OnInit {
    session: ISession;
    isSaving: boolean;

    analysts: IAnalyst[];

    scenarios: IScenario[];

    data: IData[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected sessionService: SessionService,
        protected analystService: AnalystService,
        protected scenarioService: ScenarioService,
        protected dataService: DataService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ session }) => {
            this.session = session;
        });
        this.analystService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnalyst[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnalyst[]>) => response.body)
            )
            .subscribe((res: IAnalyst[]) => (this.analysts = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.scenarioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IScenario[]>) => mayBeOk.ok),
                map((response: HttpResponse<IScenario[]>) => response.body)
            )
            .subscribe((res: IScenario[]) => (this.scenarios = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.dataService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IData[]>) => mayBeOk.ok),
                map((response: HttpResponse<IData[]>) => response.body)
            )
            .subscribe((res: IData[]) => (this.data = res), (res: HttpErrorResponse) => this.onError(res.message));
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

    trackAnalystById(index: number, item: IAnalyst) {
        return item.id;
    }

    trackScenarioById(index: number, item: IScenario) {
        return item.id;
    }

    trackDataById(index: number, item: IData) {
        return item.id;
    }
}
