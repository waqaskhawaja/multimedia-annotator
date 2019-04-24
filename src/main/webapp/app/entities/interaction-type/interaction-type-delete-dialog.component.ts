import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { InteractionTypeService } from './interaction-type.service';

@Component({
    selector: 'jhi-interaction-type-delete-dialog',
    templateUrl: './interaction-type-delete-dialog.component.html'
})
export class InteractionTypeDeleteDialogComponent {
    interactionType: IInteractionType;

    constructor(
        protected interactionTypeService: InteractionTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.interactionTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'interactionTypeListModification',
                content: 'Deleted an interactionType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-interaction-type-delete-popup',
    template: ''
})
export class InteractionTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interactionType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InteractionTypeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.interactionType = interactionType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/interaction-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/interaction-type', { outlets: { popup: null } }]);
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
