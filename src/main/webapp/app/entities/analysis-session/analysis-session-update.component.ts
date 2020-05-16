import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';
import { AnalysisSessionService } from './analysis-session.service';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from 'app/entities/analysis-scenario/analysis-scenario.service';

@Component({
    selector: 'jhi-analysis-session-update',
    templateUrl: './analysis-session-update.component.html'
})
export class AnalysisSessionUpdateComponent implements OnInit {
    analysisSession: IAnalysisSession;
    isSaving: boolean;

    analysisscenarios: IAnalysisScenario[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected analysisSessionService: AnalysisSessionService,
        protected analysisScenarioService: AnalysisScenarioService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ analysisSession }) => {
            this.analysisSession = analysisSession;
        });
        this.analysisScenarioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnalysisScenario[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnalysisScenario[]>) => response.body)
            )
            .subscribe((res: IAnalysisScenario[]) => (this.analysisscenarios = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.analysisSession.id !== undefined) {
            this.subscribeToSaveResponse(this.analysisSessionService.update(this.analysisSession));
        } else {
            this.subscribeToSaveResponse(this.analysisSessionService.create(this.analysisSession));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalysisSession>>) {
        result.subscribe((res: HttpResponse<IAnalysisSession>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAnalysisScenarioById(index: number, item: IAnalysisScenario) {
        return item.id;
    }
}
