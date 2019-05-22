import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInteractionRecord } from 'app/shared/model/interaction-record.model';
import { InteractionRecordService } from './interaction-record.service';

@Component({
    selector: 'jhi-interaction-record-delete-dialog',
    templateUrl: './interaction-record-delete-dialog.component.html'
})
export class InteractionRecordDeleteDialogComponent {
    interactionRecord: IInteractionRecord;

    constructor(
        protected interactionRecordService: InteractionRecordService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.interactionRecordService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'interactionRecordListModification',
                content: 'Deleted an interactionRecord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-interaction-record-delete-popup',
    template: ''
})
export class InteractionRecordDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interactionRecord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InteractionRecordDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.interactionRecord = interactionRecord;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/interaction-record', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/interaction-record', { outlets: { popup: null } }]);
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
