import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';

@Component({
    selector: 'jhi-analysis-session-resource-detail',
    templateUrl: './analysis-session-resource-detail.component.html'
})
export class AnalysisSessionResourceDetailComponent implements OnInit {
    analysisSessionResource: IAnalysisSessionResource;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSessionResource }) => {
            this.analysisSessionResource = analysisSessionResource;
        });
    }

    previousState() {
        window.history.back();
    }
}
