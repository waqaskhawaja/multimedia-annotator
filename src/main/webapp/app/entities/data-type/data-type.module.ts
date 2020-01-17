import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared';
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
    imports: [MaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DataTypeComponent,
        DataTypeDetailComponent,
        DataTypeUpdateComponent,
        DataTypeDeleteDialogComponent,
        DataTypeDeletePopupComponent
    ],
    entryComponents: [DataTypeComponent, DataTypeUpdateComponent, DataTypeDeleteDialogComponent, DataTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaDataTypeModule {}
