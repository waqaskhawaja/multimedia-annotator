/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MaTestModule } from '../../../test.module';
import { AnnotationSessionDeleteDialogComponent } from 'app/entities/annotation-session/annotation-session-delete-dialog.component';
import { AnnotationSessionService } from 'app/entities/annotation-session/annotation-session.service';

describe('Component Tests', () => {
    describe('AnnotationSession Management Delete Component', () => {
        let comp: AnnotationSessionDeleteDialogComponent;
        let fixture: ComponentFixture<AnnotationSessionDeleteDialogComponent>;
        let service: AnnotationSessionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnnotationSessionDeleteDialogComponent]
            })
                .overrideTemplate(AnnotationSessionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnnotationSessionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnotationSessionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
