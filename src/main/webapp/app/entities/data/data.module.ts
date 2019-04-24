import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    DataComponent,
    DataDetailComponent,
    DataUpdateComponent,
    DataDeletePopupComponent,
    DataDeleteDialogComponent,
    dataRoute,
    dataPopupRoute
} from './';

const ENTITY_STATES = [...dataRoute, ...dataPopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DataComponent, DataDetailComponent, DataUpdateComponent, DataDeleteDialogComponent, DataDeletePopupComponent],
    entryComponents: [DataComponent, DataUpdateComponent, DataDeleteDialogComponent, DataDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorDataModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
