import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
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
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DataRecordComponent,
        DataRecordDetailComponent,
        DataRecordUpdateComponent,
        DataRecordDeleteDialogComponent,
        DataRecordDeletePopupComponent
    ],
    entryComponents: [DataRecordComponent, DataRecordUpdateComponent, DataRecordDeleteDialogComponent, DataRecordDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaDataRecordModule {}
