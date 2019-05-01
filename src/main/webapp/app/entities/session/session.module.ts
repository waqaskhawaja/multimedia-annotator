import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    SessionComponent,
    SessionDetailComponent,
    SessionUpdateComponent,
    SessionDeletePopupComponent,
    SessionDeleteDialogComponent,
    sessionRoute,
    sessionPopupRoute
} from './';

const ENTITY_STATES = [...sessionRoute, ...sessionPopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SessionComponent,
        SessionDetailComponent,
        SessionUpdateComponent,
        SessionDeleteDialogComponent,
        SessionDeletePopupComponent
    ],
    entryComponents: [SessionComponent, SessionUpdateComponent, SessionDeleteDialogComponent, SessionDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorSessionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
