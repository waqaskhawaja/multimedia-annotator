import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISourceDataType } from 'app/shared/model/source-data-type.model';
import { SourceDataTypeService } from './source-data-type.service';

@Component({
    selector: 'jhi-source-data-type-delete-dialog',
    templateUrl: './source-data-type-delete-dialog.component.html'
})
export class SourceDataTypeDeleteDialogComponent {
    sourceDataType: ISourceDataType;

    constructor(
        protected sourceDataTypeService: SourceDataTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sourceDataTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sourceDataTypeListModification',
                content: 'Deleted an sourceDataType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-source-data-type-delete-popup',
    template: ''
})
export class SourceDataTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sourceDataType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SourceDataTypeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sourceDataType = sourceDataType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/source-data-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/source-data-type', { outlets: { popup: null } }]);
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
