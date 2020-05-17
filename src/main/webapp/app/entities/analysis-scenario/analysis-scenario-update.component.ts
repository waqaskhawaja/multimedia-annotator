import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAnalysisScenario, AnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';
import { DataSet } from 'app/shared/model/data-set.model';

@Component({
    selector: 'jhi-analysis-scenario-update',
    templateUrl: './analysis-scenario-update.component.html'
})
export class AnalysisScenarioUpdateComponent implements OnInit {
    isSaving: boolean;
    dataSetFile: File = null;
    fileReader = new FileReader();
    dataSets: DataSet[];

    setFileData(files: FileList) {
        this.dataSetFile = files.item(0);
        this.fileReader.readAsText(this.dataSetFile);
        this.fileReader.onload = () => {
            const jsonData: string = this.fileReader.result as string;
            this.dataSets = <DataSet[]>JSON.parse(jsonData, function(key, value) {
                if (key === 'date' && value === 'unknown') {
                    return undefined;
                }
                return value;
            });
            this.dataSets.forEach(node => {
                node.identifier = String(node.id);
                delete node.id;
            });
        };
    }

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
        analysisScenario.dataSets = this.dataSets;
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
