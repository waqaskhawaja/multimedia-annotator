import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from './data-set.service';

@Component({
    templateUrl: './data-set-delete-dialog.component.html'
})
export class DataSetDeleteDialogComponent {
    dataSet: IDataSet;

    constructor(protected dataSetService: DataSetService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dataSetService.delete(id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'dataSetListModification',
                content: 'Deleted an dataSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}
