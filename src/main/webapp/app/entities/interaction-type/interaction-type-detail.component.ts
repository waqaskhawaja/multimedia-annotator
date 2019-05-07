import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInteractionType } from 'app/shared/model/interaction-type.model';

@Component({
    selector: 'jhi-interaction-type-detail',
    templateUrl: './interaction-type-detail.component.html'
})
export class InteractionTypeDetailComponent implements OnInit {
    interactionType: IInteractionType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interactionType }) => {
            this.interactionType = interactionType;
        });
    }

    previousState() {
        window.history.back();
    }
}
