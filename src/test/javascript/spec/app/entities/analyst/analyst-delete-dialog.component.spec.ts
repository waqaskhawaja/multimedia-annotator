/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { AnalystDeleteDialogComponent } from 'app/entities/analyst/analyst-delete-dialog.component';
import { AnalystService } from 'app/entities/analyst/analyst.service';

describe('Component Tests', () => {
    describe('Analyst Management Delete Component', () => {
        let comp: AnalystDeleteDialogComponent;
        let fixture: ComponentFixture<AnalystDeleteDialogComponent>;
        let service: AnalystService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [AnalystDeleteDialogComponent]
            })
                .overrideTemplate(AnalystDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalystDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalystService);
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
