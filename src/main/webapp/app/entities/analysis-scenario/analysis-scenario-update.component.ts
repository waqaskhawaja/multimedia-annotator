import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAnalysisScenario, AnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';

@Component({
    selector: 'jhi-analysis-scenario-update',
    templateUrl: './analysis-scenario-update.component.html'
})
export class AnalysisScenarioUpdateComponent implements OnInit {
    isSaving: boolean;

    editForm = this.fb.group({
        id: [],
        name: []
    });

    constructor(
        protected analysisScenarioService: AnalysisScenarioService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ analysisScenario }) => {
            this.updateForm(analysisScenario);
        });
    }

    updateForm(analysisScenario: IAnalysisScenario) {
        this.editForm.patchValue({
            id: analysisScenario.id,
            name: analysisScenario.name
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const analysisScenario = this.createFromForm();
        if (analysisScenario.id !== undefined) {
            this.subscribeToSaveResponse(this.analysisScenarioService.update(analysisScenario));
        } else {
            this.subscribeToSaveResponse(this.analysisScenarioService.create(analysisScenario));
        }
    }

    private createFromForm(): IAnalysisScenario {
        return {
            ...new AnalysisScenario(),
            id: this.editForm.get(['id']).value,
            name: this.editForm.get(['name']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalysisScenario>>) {
        result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
