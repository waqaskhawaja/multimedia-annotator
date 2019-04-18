import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MultimediaAnnotatorSharedModule } from 'app/shared';
import {
    SourceDataTypeComponent,
    SourceDataTypeDetailComponent,
    SourceDataTypeUpdateComponent,
    SourceDataTypeDeletePopupComponent,
    SourceDataTypeDeleteDialogComponent,
    sourceDataTypeRoute,
    sourceDataTypePopupRoute
} from './';

const ENTITY_STATES = [...sourceDataTypeRoute, ...sourceDataTypePopupRoute];

@NgModule({
    imports: [MultimediaAnnotatorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SourceDataTypeComponent,
        SourceDataTypeDetailComponent,
        SourceDataTypeUpdateComponent,
        SourceDataTypeDeleteDialogComponent,
        SourceDataTypeDeletePopupComponent
    ],
    entryComponents: [
        SourceDataTypeComponent,
        SourceDataTypeUpdateComponent,
        SourceDataTypeDeleteDialogComponent,
        SourceDataTypeDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorSourceDataTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
