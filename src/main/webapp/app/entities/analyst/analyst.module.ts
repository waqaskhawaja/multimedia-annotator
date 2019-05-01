import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    AnalystComponent,
    AnalystDetailComponent,
    AnalystUpdateComponent,
    AnalystDeletePopupComponent,
    AnalystDeleteDialogComponent,
    analystRoute,
    analystPopupRoute
} from './';

const ENTITY_STATES = [...analystRoute, ...analystPopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnalystComponent,
        AnalystDetailComponent,
        AnalystUpdateComponent,
        AnalystDeleteDialogComponent,
        AnalystDeletePopupComponent
    ],
    entryComponents: [AnalystComponent, AnalystUpdateComponent, AnalystDeleteDialogComponent, AnalystDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorAnalystModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
