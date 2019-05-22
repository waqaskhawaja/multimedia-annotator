import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    InteractionRecordComponent,
    InteractionRecordDetailComponent,
    InteractionRecordUpdateComponent,
    InteractionRecordDeletePopupComponent,
    InteractionRecordDeleteDialogComponent,
    interactionRecordRoute,
    interactionRecordPopupRoute
} from './';

const ENTITY_STATES = [...interactionRecordRoute, ...interactionRecordPopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InteractionRecordComponent,
        InteractionRecordDetailComponent,
        InteractionRecordUpdateComponent,
        InteractionRecordDeleteDialogComponent,
        InteractionRecordDeletePopupComponent
    ],
    entryComponents: [
        InteractionRecordComponent,
        InteractionRecordUpdateComponent,
        InteractionRecordDeleteDialogComponent,
        InteractionRecordDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaInteractionRecordModule {}
