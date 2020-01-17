import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnotation } from 'app/shared/model/annotation.model';

@Component({
    selector: 'jhi-annotation-detail',
    templateUrl: './annotation-detail.component.html'
})
export class AnnotationDetailComponent implements OnInit {
    annotation: IAnnotation;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotation }) => {
            this.annotation = annotation;
        });
    }

    previousState() {
        window.history.back();
    }
}
