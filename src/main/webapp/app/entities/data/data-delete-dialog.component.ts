import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IData } from 'app/shared/model/data.model';
import { DataService } from './data.service';

@Component({
    selector: 'jhi-data-delete-dialog',
    templateUrl: './data-delete-dialog.component.html'
})
export class DataDeleteDialogComponent {
    data: IData;

    constructor(protected dataService: DataService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dataService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'dataListModification',
                content: 'Deleted an data'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-data-delete-popup',
    template: ''
})
export class DataDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ data }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DataDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.data = data;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/data', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/data', { outlets: { popup: null } }]);
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
