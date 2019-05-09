import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    AnnotationSessionComponent,
    AnnotationSessionDetailComponent,
    AnnotationSessionUpdateComponent,
    AnnotationSessionDeletePopupComponent,
    AnnotationSessionDeleteDialogComponent,
    annotationSessionRoute,
    annotationSessionPopupRoute
} from './';

const ENTITY_STATES = [...annotationSessionRoute, ...annotationSessionPopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnnotationSessionComponent,
        AnnotationSessionDetailComponent,
        AnnotationSessionUpdateComponent,
        AnnotationSessionDeleteDialogComponent,
        AnnotationSessionDeletePopupComponent
    ],
    entryComponents: [
        AnnotationSessionComponent,
        AnnotationSessionUpdateComponent,
        AnnotationSessionDeleteDialogComponent,
        AnnotationSessionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaAnnotationSessionModule {}
