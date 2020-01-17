import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    ScenarioComponent,
    ScenarioDetailComponent,
    ScenarioUpdateComponent,
    ScenarioDeletePopupComponent,
    ScenarioDeleteDialogComponent,
    scenarioRoute,
    scenarioPopupRoute
} from './';

const ENTITY_STATES = [...scenarioRoute, ...scenarioPopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ScenarioComponent,
        ScenarioDetailComponent,
        ScenarioUpdateComponent,
        ScenarioDeleteDialogComponent,
        ScenarioDeletePopupComponent
    ],
    entryComponents: [ScenarioComponent, ScenarioUpdateComponent, ScenarioDeleteDialogComponent, ScenarioDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaScenarioModule {}
