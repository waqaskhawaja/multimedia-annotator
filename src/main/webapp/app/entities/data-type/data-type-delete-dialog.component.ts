import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataType } from 'app/shared/model/data-type.model';
import { DataTypeService } from './data-type.service';

@Component({
    selector: 'jhi-data-type-delete-dialog',
    templateUrl: './data-type-delete-dialog.component.html'
})
export class DataTypeDeleteDialogComponent {
    dataType: IDataType;

    constructor(protected dataTypeService: DataTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dataTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'dataTypeListModification',
                content: 'Deleted an dataType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-data-type-delete-popup',
    template: ''
})
export class DataTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DataTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.dataType = dataType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/data-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/data-type', { outlets: { popup: null } }]);
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
