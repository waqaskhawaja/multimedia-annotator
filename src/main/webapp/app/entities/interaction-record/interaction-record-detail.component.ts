import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

@Component({
    selector: 'jhi-interaction-record-detail',
    templateUrl: './interaction-record-detail.component.html'
})
export class InteractionRecordDetailComponent implements OnInit {
    interactionRecord: IInteractionRecord;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interactionRecord }) => {
            this.interactionRecord = interactionRecord;
        });
    }

    previousState() {
        window.history.back();
    }
}
