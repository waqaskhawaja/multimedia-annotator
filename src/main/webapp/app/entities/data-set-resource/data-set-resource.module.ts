import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared/shared.module';
import { DataSetResourceComponent } from './data-set-resource.component';
import { DataSetResourceDetailComponent } from './data-set-resource-detail.component';
import { DataSetResourceUpdateComponent } from './data-set-resource-update.component';
import { DataSetResourceDeleteDialogComponent } from './data-set-resource-delete-dialog.component';
import { dataSetResourceRoute } from './data-set-resource.route';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(dataSetResourceRoute), ReactiveFormsModule],
    declarations: [
        DataSetResourceComponent,
        DataSetResourceDetailComponent,
        DataSetResourceUpdateComponent,
        DataSetResourceDeleteDialogComponent
    ],
    entryComponents: [DataSetResourceDeleteDialogComponent]
})
export class MaDataSetResourceModule {}
