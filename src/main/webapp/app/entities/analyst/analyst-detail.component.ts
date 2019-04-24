import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalyst } from 'app/shared/model/analyst.model';

@Component({
    selector: 'jhi-analyst-detail',
    templateUrl: './analyst-detail.component.html'
})
export class AnalystDetailComponent implements OnInit {
    analyst: IAnalyst;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analyst }) => {
            this.analyst = analyst;
        });
    }

    previousState() {
        window.history.back();
    }
}
