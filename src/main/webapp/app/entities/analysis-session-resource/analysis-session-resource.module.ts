import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    AnalysisSessionResourceComponent,
    AnalysisSessionResourceDetailComponent,
    AnalysisSessionResourceUpdateComponent,
    AnalysisSessionResourceDeletePopupComponent,
    AnalysisSessionResourceDeleteDialogComponent,
    analysisSessionResourceRoute,
    analysisSessionResourcePopupRoute
} from './';

const ENTITY_STATES = [...analysisSessionResourceRoute, ...analysisSessionResourcePopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnalysisSessionResourceComponent,
        AnalysisSessionResourceDetailComponent,
        AnalysisSessionResourceUpdateComponent,
        AnalysisSessionResourceDeleteDialogComponent,
        AnalysisSessionResourceDeletePopupComponent
    ],
    entryComponents: [
        AnalysisSessionResourceComponent,
        AnalysisSessionResourceUpdateComponent,
        AnalysisSessionResourceDeleteDialogComponent,
        AnalysisSessionResourceDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaAnalysisSessionResourceModule {}
