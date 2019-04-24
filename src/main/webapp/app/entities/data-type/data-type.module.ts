import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    DataTypeComponent,
    DataTypeDetailComponent,
    DataTypeUpdateComponent,
    DataTypeDeletePopupComponent,
    DataTypeDeleteDialogComponent,
    dataTypeRoute,
    dataTypePopupRoute
} from './';

const ENTITY_STATES = [...dataTypeRoute, ...dataTypePopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DataTypeComponent,
        DataTypeDetailComponent,
        DataTypeUpdateComponent,
        DataTypeDeleteDialogComponent,
        DataTypeDeletePopupComponent
    ],
    entryComponents: [DataTypeComponent, DataTypeUpdateComponent, DataTypeDeleteDialogComponent, DataTypeDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorDataTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
