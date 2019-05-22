import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    AnalysisSessionComponent,
    AnalysisSessionDetailComponent,
    AnalysisSessionUpdateComponent,
    AnalysisSessionDeletePopupComponent,
    AnalysisSessionDeleteDialogComponent,
    analysisSessionRoute,
    analysisSessionPopupRoute
} from './';

const ENTITY_STATES = [...analysisSessionRoute, ...analysisSessionPopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnalysisSessionComponent,
        AnalysisSessionDetailComponent,
        AnalysisSessionUpdateComponent,
        AnalysisSessionDeleteDialogComponent,
        AnalysisSessionDeletePopupComponent
    ],
    entryComponents: [
        AnalysisSessionComponent,
        AnalysisSessionUpdateComponent,
        AnalysisSessionDeleteDialogComponent,
        AnalysisSessionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaAnalysisSessionModule {}
