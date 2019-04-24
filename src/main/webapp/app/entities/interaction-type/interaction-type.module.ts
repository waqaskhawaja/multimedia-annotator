import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
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
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
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
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorInteractionTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
