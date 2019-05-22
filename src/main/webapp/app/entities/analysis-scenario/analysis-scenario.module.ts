import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    AnalysisScenarioComponent,
    AnalysisScenarioDetailComponent,
    AnalysisScenarioUpdateComponent,
    AnalysisScenarioDeletePopupComponent,
    AnalysisScenarioDeleteDialogComponent,
    analysisScenarioRoute,
    analysisScenarioPopupRoute
} from './';

const ENTITY_STATES = [...analysisScenarioRoute, ...analysisScenarioPopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnalysisScenarioComponent,
        AnalysisScenarioDetailComponent,
        AnalysisScenarioUpdateComponent,
        AnalysisScenarioDeleteDialogComponent,
        AnalysisScenarioDeletePopupComponent
    ],
    entryComponents: [
        AnalysisScenarioComponent,
        AnalysisScenarioUpdateComponent,
        AnalysisScenarioDeleteDialogComponent,
        AnalysisScenarioDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaAnalysisScenarioModule {}
