import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnalyst } from 'app/shared/model/analyst.model';
import { AnalystService } from './analyst.service';

@Component({
    selector: 'jhi-analyst-delete-dialog',
    templateUrl: './analyst-delete-dialog.component.html'
})
export class AnalystDeleteDialogComponent {
    analyst: IAnalyst;

    constructor(protected analystService: AnalystService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.analystService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'analystListModification',
                content: 'Deleted an analyst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-analyst-delete-popup',
    template: ''
})
export class AnalystDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analyst }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnalystDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.analyst = analyst;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/analyst', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/analyst', { outlets: { popup: null } }]);
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
