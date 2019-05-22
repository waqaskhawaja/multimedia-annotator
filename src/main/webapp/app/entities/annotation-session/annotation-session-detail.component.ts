import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnotationSession } from 'app/shared/model/annotation-session.model';

@Component({
    selector: 'jhi-annotation-session-detail',
    templateUrl: './annotation-session-detail.component.html'
})
export class AnnotationSessionDetailComponent implements OnInit {
    annotationSession: IAnnotationSession;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
    }

    previousState() {
        window.history.back();
    }
}
