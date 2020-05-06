import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';

@Component({
    templateUrl: './annotation-type-delete-dialog.component.html'
})
export class AnnotationTypeDeleteDialogComponent {
    annotationType: IAnnotationType;

    constructor(
        protected annotationTypeService: AnnotationTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.annotationTypeService.delete(id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'annotationTypeListModification',
                content: 'Deleted an annotationType'
            });
            this.activeModal.dismiss(true);
        });
    }
}
