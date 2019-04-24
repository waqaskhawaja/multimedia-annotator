import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    DataRecordComponent,
    DataRecordDetailComponent,
    DataRecordUpdateComponent,
    DataRecordDeletePopupComponent,
    DataRecordDeleteDialogComponent,
    dataRecordRoute,
    dataRecordPopupRoute
} from './';

const ENTITY_STATES = [...dataRecordRoute, ...dataRecordPopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DataRecordComponent,
        DataRecordDetailComponent,
        DataRecordUpdateComponent,
        DataRecordDeleteDialogComponent,
        DataRecordDeletePopupComponent
    ],
    entryComponents: [DataRecordComponent, DataRecordUpdateComponent, DataRecordDeleteDialogComponent, DataRecordDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorDataRecordModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
