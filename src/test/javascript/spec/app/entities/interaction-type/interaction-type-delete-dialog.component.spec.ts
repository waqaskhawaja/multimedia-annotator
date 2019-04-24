/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { InteractionTypeDeleteDialogComponent } from 'app/entities/interaction-type/interaction-type-delete-dialog.component';
import { InteractionTypeService } from 'app/entities/interaction-type/interaction-type.service';

describe('Component Tests', () => {
    describe('InteractionType Management Delete Component', () => {
        let comp: InteractionTypeDeleteDialogComponent;
        let fixture: ComponentFixture<InteractionTypeDeleteDialogComponent>;
        let service: InteractionTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [InteractionTypeDeleteDialogComponent]
            })
                .overrideTemplate(InteractionTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InteractionTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InteractionTypeService);
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
