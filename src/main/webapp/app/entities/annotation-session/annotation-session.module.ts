import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgxYoutubePlayerModule } from 'ngx-youtube-player';

import { Ng5SliderModule } from 'ng5-slider';
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
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatPaginatorModule } from '@angular/material/paginator';

const ENTITY_STATES = [...annotationSessionRoute, ...annotationSessionPopupRoute];

@NgModule({
    imports: [
        Ng5SliderModule,
        MaSharedModule,
        NgxYoutubePlayerModule.forRoot(),
        RouterModule.forChild(ENTITY_STATES),
        MatTableModule,
        MatCheckboxModule,
        MatProgressBarModule,
        MatPaginatorModule
    ],
    exports: [MatTableModule, MatCheckboxModule],

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
