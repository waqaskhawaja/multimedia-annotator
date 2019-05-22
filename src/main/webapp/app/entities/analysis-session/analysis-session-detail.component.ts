import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

@Component({
    selector: 'jhi-analysis-session-detail',
    templateUrl: './analysis-session-detail.component.html'
})
export class AnalysisSessionDetailComponent implements OnInit {
    analysisSession: IAnalysisSession;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSession }) => {
            this.analysisSession = analysisSession;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
