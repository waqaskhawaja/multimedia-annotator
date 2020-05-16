import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared/shared.module';
import { DataSetComponent } from './data-set.component';
import { DataSetDetailComponent } from './data-set-detail.component';
import { DataSetUpdateComponent } from './data-set-update.component';
import { DataSetDeleteDialogComponent } from './data-set-delete-dialog.component';
import { dataSetRoute } from './data-set.route';

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(dataSetRoute)],
    declarations: [DataSetComponent, DataSetDetailComponent, DataSetUpdateComponent, DataSetDeleteDialogComponent],
    entryComponents: [DataSetDeleteDialogComponent]
})
export class MaDataSetModule {}
