import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataRecord } from 'app/shared/model/data-record.model';
import { DataRecordService } from './data-record.service';

@Component({
    selector: 'jhi-data-record-delete-dialog',
    templateUrl: './data-record-delete-dialog.component.html'
})
export class DataRecordDeleteDialogComponent {
    dataRecord: IDataRecord;

    constructor(
        protected dataRecordService: DataRecordService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dataRecordService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'dataRecordListModification',
                content: 'Deleted an dataRecord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-data-record-delete-popup',
    template: ''
})
export class DataRecordDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataRecord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DataRecordDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.dataRecord = dataRecord;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/data-record', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/data-record', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
