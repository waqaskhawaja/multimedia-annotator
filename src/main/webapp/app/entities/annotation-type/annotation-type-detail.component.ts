import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnotationType } from 'app/shared/model/annotation-type.model';

@Component({
    selector: 'jhi-annotation-type-detail',
    templateUrl: './annotation-type-detail.component.html'
})
export class AnnotationTypeDetailComponent implements OnInit {
    annotationType: IAnnotationType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationType }) => {
            this.annotationType = annotationType;
        });
    }

    previousState() {
        window.history.back();
    }
}
