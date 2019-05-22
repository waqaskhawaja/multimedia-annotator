import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';

@Component({
    selector: 'jhi-analysis-scenario-update',
    templateUrl: './analysis-scenario-update.component.html'
})
export class AnalysisScenarioUpdateComponent implements OnInit {
    analysisScenario: IAnalysisScenario;
    isSaving: boolean;

    constructor(protected analysisScenarioService: AnalysisScenarioService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ analysisScenario }) => {
            this.analysisScenario = analysisScenario;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.analysisScenario.id !== undefined) {
            this.subscribeToSaveResponse(this.analysisScenarioService.update(this.analysisScenario));
        } else {
            this.subscribeToSaveResponse(this.analysisScenarioService.create(this.analysisScenario));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalysisScenario>>) {
        result.subscribe((res: HttpResponse<IAnalysisScenario>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
