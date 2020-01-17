import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

@Component({
    selector: 'jhi-analysis-session-detail',
    templateUrl: './analysis-session-detail.component.html'
})
export class AnalysisSessionDetailComponent implements OnInit {
    analysisSession: IAnalysisSession;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSession }) => {
            this.analysisSession = analysisSession;
        });
    }

    previousState() {
        window.history.back();
    }
}
