import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
import {
    InteractionTypeComponent,
    InteractionTypeDetailComponent,
    InteractionTypeUpdateComponent,
    InteractionTypeDeletePopupComponent,
    InteractionTypeDeleteDialogComponent,
    interactionTypeRoute,
    interactionTypePopupRoute
} from './';

const ENTITY_STATES = [...interactionTypeRoute, ...interactionTypePopupRoute];

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InteractionTypeComponent,
        InteractionTypeDetailComponent,
        InteractionTypeUpdateComponent,
        InteractionTypeDeleteDialogComponent,
        InteractionTypeDeletePopupComponent
    ],
    entryComponents: [
        InteractionTypeComponent,
        InteractionTypeUpdateComponent,
        InteractionTypeDeleteDialogComponent,
        InteractionTypeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaInteractionTypeModule {}
