import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
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
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ScenarioComponent,
        ScenarioDetailComponent,
        ScenarioUpdateComponent,
        ScenarioDeleteDialogComponent,
        ScenarioDeletePopupComponent
    ],
    entryComponents: [ScenarioComponent, ScenarioUpdateComponent, ScenarioDeleteDialogComponent, ScenarioDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorScenarioModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
