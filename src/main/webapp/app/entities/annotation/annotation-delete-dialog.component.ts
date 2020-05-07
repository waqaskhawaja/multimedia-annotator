import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnnotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from './annotation.service';

@Component({
    templateUrl: './annotation-delete-dialog.component.html'
})
export class AnnotationDeleteDialogComponent {
    annotation: IAnnotation;

    constructor(
        protected annotationService: AnnotationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.annotationService.delete(id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'annotationListModification',
                content: 'Deleted an annotation'
            });
            this.activeModal.dismiss(true);
        });
    }
}
