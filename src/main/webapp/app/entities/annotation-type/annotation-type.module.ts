import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    AnnotationTypeComponent,
    AnnotationTypeDetailComponent,
    AnnotationTypeUpdateComponent,
    AnnotationTypeDeletePopupComponent,
    AnnotationTypeDeleteDialogComponent,
    annotationTypeRoute,
    annotationTypePopupRoute
} from './';

const ENTITY_STATES = [...annotationTypeRoute, ...annotationTypePopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnnotationTypeComponent,
        AnnotationTypeDetailComponent,
        AnnotationTypeUpdateComponent,
        AnnotationTypeDeleteDialogComponent,
        AnnotationTypeDeletePopupComponent
    ],
    entryComponents: [
        AnnotationTypeComponent,
        AnnotationTypeUpdateComponent,
        AnnotationTypeDeleteDialogComponent,
        AnnotationTypeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaAnnotationTypeModule {}
