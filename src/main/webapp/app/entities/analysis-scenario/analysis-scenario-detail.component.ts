import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

@Component({
    selector: 'jhi-analysis-scenario-detail',
    templateUrl: './analysis-scenario-detail.component.html'
})
export class AnalysisScenarioDetailComponent implements OnInit {
    analysisScenario: IAnalysisScenario;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisScenario }) => {
            this.analysisScenario = analysisScenario;
        });
    }

    previousState() {
        window.history.back();
    }
}
