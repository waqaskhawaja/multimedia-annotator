import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSetResourceService } from './data-set-resource.service';

@Component({
    templateUrl: './data-set-resource-delete-dialog.component.html'
})
export class DataSetResourceDeleteDialogComponent {
    dataSetResource: IDataSetResource;

    constructor(
        protected dataSetResourceService: DataSetResourceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dataSetResourceService.delete(id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'dataSetResourceListModification',
                content: 'Deleted an dataSetResource'
            });
            this.activeModal.dismiss(true);
        });
    }
}
